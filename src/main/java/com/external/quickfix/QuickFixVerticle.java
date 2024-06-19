package com.external.quickfix;

import com.external.rfq.KourNetOrder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import java.io.InputStream;
import org.pmw.tinylog.Logger;
import quickfix.*;
import quickfix.field.Account;
import quickfix.field.OrderQty;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteType;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.Text;
import quickfix.fix44.QuoteRequest;

public class QuickFixVerticle extends AbstractVerticle {

  private final InputStream fixConfig;
  private final QuickFixEngine engine;


  public QuickFixVerticle(InputStream fixConfig) {
    this.fixConfig = fixConfig;
    this.engine = new QuickFixEngine();
  }

  @Override
  public void start() {
    Logger.info("Started QuickFixVerticle");
    createFIXSession();
    setupEventBusHandlers();
  }

  private void createFIXSession() {
    try {
      SessionSettings settings = new SessionSettings(fixConfig);
      MessageStoreFactory storeFactory = new MemoryStoreFactory();
      LogFactory logFactory = new SLF4JLogFactory(settings);
      MessageFactory messageFactory = new DefaultMessageFactory();
      Acceptor acceptor = new SocketAcceptor(engine, storeFactory, settings, logFactory,
          messageFactory);
      acceptor.start();
    } catch (Exception e) {
      Logger.error(e, "Some error encountered creating FIX session");
    }
  }

  private void setupEventBusHandlers() {
    vertx.eventBus().consumer("NEW_ORDER", this::sendNewOrder);
  }

  //TODO this should be moved into QuickFixEngine actually, that way we can store pending messages inside the engine
  //TODO and send them once we'relogged in, and we don't need loggedOn() to be accessed from the verticle
  private void sendNewOrder(Message<KourNetOrder> message) {
    KourNetOrder order = message.body();

    if (!engine.isLoggedOn()) {
      Logger.error("Tried to send {} but not logged on!", order);
    }

    try {
      quickfix.fix44.QuoteRequest quoteRequest = new QuoteRequest();
      quoteRequest.set(new QuoteReqID("REQ"));
      Group grp = new Group(new QuoteRequest.NoRelatedSym());
      grp.setField(new Side(Side.BUY));
      grp.setField(new QuoteType(QuoteType.TRADEABLE));
      grp.setField(new OrderQty(order.getOrderQty()));
      grp.setField(new Symbol(order.getItem()));
      grp.setField(new Account(order.getCounterparty()));
      quoteRequest.addGroup(grp);

      quoteRequest.set(new Text("new order!"));
      //sendercompid, targetcomp id
      boolean sent = Session.sendToTarget(quoteRequest, "kournet", "jadstreet");
    } catch (Exception e) {
      Logger.error(e, "Some error while trying to send new RFQ out the door");
    }
  }
}

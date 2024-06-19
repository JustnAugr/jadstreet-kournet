package com.external.temp;

import com.external.KourNetApplication;
import io.vertx.core.AbstractVerticle;
import org.pmw.tinylog.Logger;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.QuoteRequest;

//temporary class to just receive and print out FIX msgs
public class TempJadStreetVerticle extends AbstractVerticle {

  boolean messageSent = false;

  @Override
  public void start() {
    Logger.info("Started TempJadStreetVerticle");

    try {
      Application app = new FooApplication();

      SessionSettings settings = new SessionSettings(
          KourNetApplication.class.getResourceAsStream("/JadStreet.properties"));
      MessageStoreFactory storeFactory = new MemoryStoreFactory();
      LogFactory logFactory = new SLF4JLogFactory(settings);
      MessageFactory messageFactory = new DefaultMessageFactory();
      Initiator initiator = new SocketInitiator(app, storeFactory, settings, logFactory,
          messageFactory);
      initiator.start();
    } catch (Exception e) {
      Logger.error(e);
    }
  }
}

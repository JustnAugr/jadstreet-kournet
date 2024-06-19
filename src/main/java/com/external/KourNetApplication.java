package com.external;

import com.external.quickfix.QuickFixVerticle;
import com.external.rfq.KourNetOrder;
import com.external.rfq.KourNetOrder.KourNetOrderCodec;
import com.external.rfq.QuoteGenVerticle;
import com.external.temp.TempJadStreetVerticle;
import io.vertx.core.Vertx;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.pmw.tinylog.Logger;

public class KourNetApplication {

  public static void main(String[] args) throws IOException, InterruptedException {
    Logger.info("Running main Application");

    Vertx vertx = Vertx.vertx();
    registerEventBusCodecs(vertx);

    vertx.deployVerticle(new QuickFixVerticle(getKourNetFixConfig()));
    vertx.deployVerticle(new TempJadStreetVerticle());
    Thread.sleep(10000);
    vertx.deployVerticle(new QuoteGenVerticle(getQuoteGenProperties()));
  }

  private static void registerEventBusCodecs(Vertx vertx) {
    vertx.eventBus().registerDefaultCodec(KourNetOrder.class, new KourNetOrderCodec());
  }

  private static InputStream getKourNetFixConfig() {
    return KourNetApplication.class.getResourceAsStream("/KourNet.properties");
  }

  private static Properties getQuoteGenProperties() throws IOException {
    Properties properties = new Properties();
    InputStream stream = KourNetApplication.class.getResourceAsStream("/config.properties");
    properties.load(stream);
    return properties;
  }
}

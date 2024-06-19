package com.external.rfq;

import io.vertx.core.AbstractVerticle;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.pmw.tinylog.Logger;

public class QuoteGenVerticle extends AbstractVerticle {

  private final Properties properties;
  private final List<String> clients;
  private final List<String> items;

  public QuoteGenVerticle(Properties properties) {
    this.properties = properties;
    clients = Arrays.asList(properties.getProperty("clients").split(","));
    items = Arrays.asList(properties.getProperty("items").split(", "));
  }

  @Override
  public void start() throws Exception {
    Logger.info("QuoteGenVerticle started!");

    //get a random client and a random product
    Random rand = new Random();
    String client = clients.get(rand.nextInt(clients.size()));
    String product = items.get(rand.nextInt(items.size()));
    double size = rand.nextInt(1, 1000) * 1e6 * 1.0; //1m to 100m

    KourNetOrder order = new KourNetOrder(client, product, size);
    vertx.eventBus().send("NEW_ORDER", order);
  }
}

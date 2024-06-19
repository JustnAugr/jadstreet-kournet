package com.external.rfq;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class KourNetOrder {

  private final String counterparty;
  private final String item;
  private final double orderQty;

  public KourNetOrder(String counterparty, String item, double orderQty) {
    this.counterparty = counterparty;
    this.item = item;
    this.orderQty = orderQty;
  }

  public String getCounterparty() {
    return counterparty;
  }

  public String getItem() {
    return item;
  }

  public double getOrderQty() {
    return orderQty;
  }

  //Messaging Codec for KourNetOrder only being used for intra-JVM verticle communication
  public static class KourNetOrderCodec implements MessageCodec<KourNetOrder, KourNetOrder> {

    @Override
    public void encodeToWire(Buffer buffer, KourNetOrder kourNetOrder) {
    }

    @Override
    public KourNetOrder decodeFromWire(int i, Buffer buffer) {
      return null;
    }

    @Override
    public KourNetOrder transform(KourNetOrder kourNetOrder) {
      return kourNetOrder;
    }

    @Override
    public String name() {
      return this.getClass().getName();
    }

    @Override
    public byte systemCodecID() {
      return -1;
    }
  }
}

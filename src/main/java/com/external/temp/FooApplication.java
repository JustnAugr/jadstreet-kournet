package com.external.temp;

import org.pmw.tinylog.Logger;
import quickfix.*;

public class FooApplication implements Application {

  @Override
  public void onCreate(SessionID sessionID) {

  }

  @Override
  public void onLogon(SessionID sessionID) {

  }

  @Override
  public void onLogout(SessionID sessionID) {

  }

  @Override
  public void toAdmin(Message message, SessionID sessionID) {

  }

  @Override
  public void fromAdmin(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

  }

  @Override
  public void toApp(Message message, SessionID sessionID) throws DoNotSend {
    String m = message.toString();
    Logger.info("Sending a message to KourNet: {}", m);
  }

  @Override
  public void fromApp(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    String m = message.toString();
    Logger.info("Got a message from KourNet: {}", m);
  }
}

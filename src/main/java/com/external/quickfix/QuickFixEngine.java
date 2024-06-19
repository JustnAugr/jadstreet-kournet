package com.external.quickfix;

import org.pmw.tinylog.Logger;
import quickfix.*;

public class QuickFixEngine implements Application {
  private boolean loggedOn = false;

  /*
  When a new session is created
  - this is even if we haven't connected to any dealers
  - we can queue msgs to any created session and an eventual logon will have those get sent out
   */
  @Override
  public void onCreate(SessionID sessionID) {
    Logger.info("Session created:" + sessionID.toString());
  }

  /*
  When both parties exchange valid logon messages (after the original session create)
   */
  @Override
  public void onLogon(SessionID sessionID) {
    Logger.info("Session logged on " + sessionID.toString());
    this.loggedOn = true;
  }

  /*
  This callback notifies you when an FIX session is no longer online. This could happen during a
  normal logout exchange or because of a forced termination or a loss of network connection.
   */
  @Override
  public void onLogout(SessionID sessionID) {
    Logger.info("Session logged out " + sessionID.toString());
    this.loggedOn = false;
  }

  /*
  admin (login) messages sent between FIX engine and counterparties
   */
  @Override
  public void toAdmin(Message message, SessionID sessionID) {
    Logger.info("toAdmin " + message.toString() + " " + sessionID.toString());
  }

  /*
  admin (login) messages sent between FIX engine and counterparties
   */
  @Override
  public void fromAdmin(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    Logger.info("fromAdmin " + message.toString() + " " + sessionID.toString());
  }

  /*
  callback for msgs sent from KourNet to brokers
   */
  @Override
  public void toApp(Message message, SessionID sessionID) throws DoNotSend {
    Logger.info("toApp " + message.toString() + " " + sessionID.toString());
  }

  /*
   callback for messages received from brokers to KourNet
   FieldNotFound/UnsupportedMessageType/IncorrectTagValue exceptions will return
   rejects to the brokers
   */
  @Override
  public void fromApp(Message message, SessionID sessionID)
      throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    Logger.info("fromApp " + message.toString() + " " + sessionID.toString());
  }

  public boolean isLoggedOn() {
    return loggedOn;
  }
}

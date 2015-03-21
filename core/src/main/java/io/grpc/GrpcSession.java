package io.grpc;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.net.SocketAddress;

/**
 * Holds state pertaining to a single transport connection
 */
public class GrpcSession {

  private final SocketAddress remoteAddress;
  private final SSLEngine sslEngine;

  public GrpcSession(SocketAddress remoteAddress, SSLEngine sslEngine) {
    this.remoteAddress = remoteAddress;
    this.sslEngine = sslEngine;
  }

  public SocketAddress getRemoteAddress() {
    return remoteAddress;
  }

  public SSLSession getSslSession() {
    SSLSession session = null;
    if (sslEngine != null) {
      session = sslEngine.getSession();
    }
    return session;
  }

  static final ThreadLocal<GrpcSession> THREAD_LOCAL = new ThreadLocal<GrpcSession>();

  /**
   * Gets the active GrpcSession (from the ThreadLocal)
   *
   * @return active GrpcSession
   */
  public static GrpcSession get() {
    GrpcSession session = THREAD_LOCAL.get();
    assert session != null;
    return session;
  }

  /**
   * Sets the active GrpcSession (sets the ThreadLocal)
   *
   * Should only be called when no GrpcSession is active.
   *
   * @param session GrpcSession to set as active
   */
  static void enter(GrpcSession session) {
    assert THREAD_LOCAL.get() == null;
    THREAD_LOCAL.set(session);
  }

  /**
   * Gets the active GrpcSession (clears the ThreadLocal)
   *
   * Should only be called when a GrpcSession is active.
   */
  static void exit() {
    assert THREAD_LOCAL.get() != null;
    THREAD_LOCAL.set(null);
  }

}

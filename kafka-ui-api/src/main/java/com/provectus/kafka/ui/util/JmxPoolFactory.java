package com.provectus.kafka.ui.util;

import java.io.IOException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.provectus.kafka.ui.model.JmxConnectionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

@Slf4j
public class JmxPoolFactory extends BaseKeyedPooledObjectFactory<JmxConnectionInfo, JMXConnector> {

  @Override
  public JMXConnector create(JmxConnectionInfo info) throws Exception {
    return JMXConnectorFactory.connect(new JMXServiceURL(info.getUrl()));
  }

  @Override
  public PooledObject<JMXConnector> wrap(JMXConnector jmxConnector) {
    return new DefaultPooledObject<>(jmxConnector);
  }

  @Override
  public void destroyObject(JmxConnectionInfo key, PooledObject<JMXConnector> p) {
    try {
      p.getObject().close();
    } catch (IOException e) {
      log.error("Cannot close connection with {}", key);
    }
  }
}

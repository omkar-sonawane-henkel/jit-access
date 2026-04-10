package com.google.solutions.jitaccess.core.catalog.project;

import java.util.regex.Pattern;

import com.google.solutions.jitaccess.core.catalog.ActivationType;
import com.google.solutions.jitaccess.core.catalog.ExternalApproval;
import com.google.solutions.jitaccess.core.catalog.NoActivation;
import com.google.solutions.jitaccess.core.catalog.PeerApproval;
import com.google.solutions.jitaccess.core.catalog.SelfApproval;

public class ActivationTypeFactory {

  public static ActivationType createFromName(String name) {
    if (name.startsWith("NONE")) {
      return new NoActivation();
    } else if (name.startsWith("SELF_APPROVAL")) {
      return new SelfApproval();
    } else if (name.startsWith("PEER_APPROVAL")) {
      var topic = name.substring("PEER_APPROVAL(".length(), name.length() - ")".length());
      validateTopicPeer(topic);
      return new PeerApproval(topic);
    } else if (name.startsWith("EXTERNAL_APPROVAL")) {
      var topic = name.substring("EXTERNAL_APPROVAL(".length(), name.length() - ")".length());
      validateTopicExternal(topic);
      return new ExternalApproval(topic);
    } else {
      throw new IllegalArgumentException("Invalid activation type name.");
    }
  }

  private static void validateTopicPeer(String topic) {
    if (topic.trim().equals("")) {
      return;
    }

    if (!Pattern.compile(PrivilegeFactory.VALID_SINGLE_TOPIC_PATTERN).matcher("." + topic).matches()) {
      throw new IllegalArgumentException("Invalid topic name.");
    }
  }

  private static void validateTopicExternal(String topic) {
    if (topic.trim().equals("")) {
      return;
    }

    if (!Pattern.compile(PrivilegeFactory.VALID_HIERARCHICAL_TOPIC_PATTERN).matcher("." + topic).matches()) {
      throw new IllegalArgumentException("Invalid topic name.");
    }
  }
}

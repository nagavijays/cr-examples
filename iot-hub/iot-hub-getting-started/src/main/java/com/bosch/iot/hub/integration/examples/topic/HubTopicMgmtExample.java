/*
 * Bosch SI Example Code License Version 1.0, January 2016
 *
 * Copyright 2016 Bosch Software Innovations GmbH ("Bosch SI"). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * BOSCH SI PROVIDES THE PROGRAM "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL
 * NECESSARY SERVICING, REPAIR OR CORRECTION. THIS SHALL NOT APPLY TO MATERIAL DEFECTS AND DEFECTS OF TITLE WHICH BOSCH
 * SI HAS FRAUDULENTLY CONCEALED. APART FROM THE CASES STIPULATED ABOVE, BOSCH SI SHALL BE LIABLE WITHOUT LIMITATION FOR
 * INTENT OR GROSS NEGLIGENCE, FOR INJURIES TO LIFE, BODY OR HEALTH AND ACCORDING TO THE PROVISIONS OF THE GERMAN
 * PRODUCT LIABILITY ACT (PRODUKTHAFTUNGSGESETZ). THE SCOPE OF A GUARANTEE GRANTED BY BOSCH SI SHALL REMAIN UNAFFECTED
 * BY LIMITATIONS OF LIABILITY. IN ALL OTHER CASES, LIABILITY OF BOSCH SI IS EXCLUDED. THESE LIMITATIONS OF LIABILITY
 * ALSO APPLY IN REGARD TO THE FAULT OF VICARIOUS AGENTS OF BOSCH SI AND THE PERSONAL LIABILITY OF BOSCH SI'S EMPLOYEES,
 * REPRESENTATIVES AND ORGANS.
 */
package com.bosch.iot.hub.integration.examples.topic;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bosch.iot.hub.integration.examples.util.HubClientUtil;

import com.bosch.iot.hub.client.IotHubClient;
import com.bosch.iot.hub.model.topic.TopicPath;


/**
 * Preconditions of running the example :
 * <ol>
 * <li>Register your solution to get solution_id, and upload the public key from /HubExampleClient.jks (or create your
 * own Key-pair)</li>
 * <li>Use solution_id as your system property "SOLUTION_ID"</li>
 * <li>Configure system property "HUB_CLOUD_ENDPOINT", using actual Websocket endpoint of IoT Hub Service</li>
 * <li>Configure system property "PROXY_URI" if you have one, using format http://host:port</li>
 * </ol>
 *
 *Examples of System Properties:
 * <br/>
 * <strong> -DSOLUTION_ID=xx -DHUB_CLOUD_ENDPOINT=wss://xx.com -DPROXY_URI=http://xx.com</strong>

 */
public final class HubTopicMgmtExample
{

   public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException
   {
      // init hub client, connect to backend
      IotHubClient solutionClient = HubClientUtil.initSolutionClient(HubClientUtil.SOLUTION_CLIENT_ID);
      solutionClient.connect();

      // create solution root topic
      solutionClient.createTopic(HubClientUtil.SOLUTION_TOPIC).get(HubClientUtil.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

      // create solution sub topic
      TopicPath subTopicPath = TopicPath.of(HubClientUtil.SOLUTION_SUBTOPIC);
      solutionClient.createTopic(subTopicPath).thenAccept(topic -> {
         // you can do something here.
         System.out.println("A sub topic is created with path " + topic.getPath().toString());
      }).get(10, TimeUnit.SECONDS);

      // remove the root topic of the solution
      solutionClient.deleteTopic(HubClientUtil.SOLUTION_TOPIC).get(HubClientUtil.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

      // disconnect solution
      solutionClient.destroy();
   }
}

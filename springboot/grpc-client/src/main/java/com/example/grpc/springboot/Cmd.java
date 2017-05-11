/*
 * Copyright 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.grpc.springboot;

import com.example.echo.EchoOuterClass;
import com.example.echo.EchoServiceGrpc;
import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.grpc.client.GrpcChannelFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by rayt on 5/18/16.
 */
@Component
@EnableDiscoveryClient
public class Cmd {
	private int i = 0;
	private GrpcChannelFactory channelFactory;
	private DiscoveryClient discoveryClient;

  @Autowired
  public Cmd(@Qualifier("discoveryClientChannelFactory") GrpcChannelFactory channelFactory,
						 DiscoveryClient discoveryClient ) {
  	this.channelFactory = channelFactory;
  	this.discoveryClient = discoveryClient;
  }

	@Scheduled(fixedDelay = 5000)
	public void requestRegular() {
		System.out.println("hello");
		Channel channel = channelFactory.createChannel("EchoService");
//		discoveryClient.getServices();

    i++;
		EchoServiceGrpc.EchoServiceBlockingStub stub = EchoServiceGrpc.newBlockingStub(channel);


	  /**
	   * 2017-05-11 13:28:43 [pool-8-thread-1] ERROR [TaskUtils.java:95] - Unexpected error occurred in scheduled task.
	   io.grpc.StatusRuntimeException: INTERNAL: Connection closed with unknown cause
	   at io.grpc.stub.ClientCalls.toStatusRuntimeException(ClientCalls.java:230)
	   at io.grpc.stub.ClientCalls.getUnchecked(ClientCalls.java:211)
	   at io.grpc.stub.ClientCalls.blockingUnaryCall(ClientCalls.java:144)
	   at com.example.echo.EchoServiceGrpc$EchoServiceBlockingStub.echo(EchoServiceGrpc.java:135)
	   at com.example.grpc.springboot.Cmd.requestRegular(Cmd.java:55)
	   at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	   at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	   at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	   at java.lang.reflect.Method.invoke(Method.java:497)
	   at org.springframework.scheduling.support.ScheduledMethodRunnable.run(ScheduledMethodRunnable.java:65)
	   at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	   at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	   at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308)
	   at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180)
	   at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294)
	   at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	   at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	   at java.lang.Thread.run(Thread.java:745)
	   */
		EchoOuterClass.Echo response = stub.echo(EchoOuterClass.Echo.newBuilder().setMessage("Hello " + i).build());


		System.out.println(response);
	}
}

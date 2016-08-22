package com.se2.junit.runner;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.se2.junit.test.TestClientByProject;

public class TestClientByProjectRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(TestClientByProject.class);
		
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println(result.wasSuccessful());
   }
}  	
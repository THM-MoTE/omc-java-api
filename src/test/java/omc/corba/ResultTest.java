/**
 * Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
 */

package omc.corba;

import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResultTest {

  @Test
  public void toStringTest() {
    Result result = new Result("test", Optional.empty());
    assertEquals("Result [result=test, error="+Optional.empty().toString()+"]", result.toString());
    Result result2 = new Result("nico", Optional.of("an error"));
    assertEquals("Result [result=nico, error="+Optional.of("an error").toString()+"]", result2.toString());
  }
  
  @Test
  public void equalsTest() {
    Result result = new Result("test", Optional.empty());
    Result result2 = new Result("test", Optional.empty());
    Result result3 = new Result("test", Optional.of("bla"));
    Result result4 = new Result("bla", Optional.of("bla2"));
    assertEquals(result, result2);
    assertNotEquals(result, result3);
    assertNotEquals(result, result4);
  }
  
  @Test
  public void hashCodeTest() {
    Result result = new Result("test", Optional.empty());
    Result result2 = new Result("test", Optional.empty());
    Result result3 = new Result("test", Optional.of("bla"));
    assertEquals(result.hashCode(), result2.hashCode());
    assertNotEquals(result.hashCode(), result3.hashCode());
  }
}

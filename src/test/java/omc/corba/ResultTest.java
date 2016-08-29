/** Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package omc.corba;

import java.util.Optional;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class ResultTest {

  @Test()
  public void toStringTest() {
    Result result = new Result("test", Optional.empty());
    assertEquals(result.toString(), "Result [result=test, error="+Optional.empty().toString()+"]");
    Result result2 = new Result("nico", Optional.of("an error"));
    assertEquals(result2.toString(), "Result [result=nico, error="+Optional.of("an error").toString()+"]");
  }

  @Test()
  public void equalsTest() {
    Result result = new Result("test", Optional.empty());
    Result result2 = new Result("test", Optional.empty());
    Result result3 = new Result("test", Optional.of("bla"));
    Result result4 = new Result("bla", Optional.of("bla2"));
    assertEquals(result2, result);
    assertNotEquals(result3, result);
    assertNotEquals(result4, result);
  }

  @Test()
  public void hashCodeTest() {
    Result result = new Result("test", Optional.empty());
    Result result2 = new Result("test", Optional.empty());
    Result result3 = new Result("test", Optional.of("bla"));
    assertEquals(result2.hashCode(), result.hashCode());
    assertNotEquals(result3.hashCode(), result.hashCode());
  }
}

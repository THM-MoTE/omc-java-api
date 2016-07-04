package omc.corba;

import java.util.List;

public class Result {
	public final String result;
	public final List<String> errors;

	public Result(String result, List<String> errors) {
	  super();
	  this.result = result;
	  this.errors = errors;
  }
}

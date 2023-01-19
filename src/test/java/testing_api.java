import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.testng.Assert;


import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class testing_api {
	@Test
	void test1()
	{
		Response response = RestAssured.get("http://test-api.superzop.com:3000/retailer?\r\n"
				+ "phone={{phone}}&retailer_id={{retailer_id}}&token={{Token}}");
		System.out.println(response.getStatusCode()); 
		//Assert.assertEquals(response.getStatusCode(), 404);
		System.out.println(response.asPrettyString());
		
		given().
		header("content-type","application/json").
			get("https://reqres.in/api/users?page=2").
			then().statusCode(200).body("data.id[1]",equalTo(8)).
			body("data.first_name",hasItems("Michael","Lindsay"));
	}
}

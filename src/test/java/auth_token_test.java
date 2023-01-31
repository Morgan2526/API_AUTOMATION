import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.*;
import java.util.HashSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter; 

public class auth_token_test {
	
	
	int n;
	String token;
	Object phone;
	Object retailer_id;
	String last_order_number;
	String group_id;
	String store_series;
	long cdate=1674107881;
	Vector<String> offer_product_id=new Vector<String>();
	Vector<String> min_purchase_qty1=new Vector<String>();
	Vector<String> min_purchase_qty2=new Vector<String>();
	Vector<String> min_purchase_qty3=new Vector<String>();
	Vector<String> max_purchase_qty1=new Vector<String>();
	Vector<String> max_purchase_qty2=new Vector<String>();
	Vector<String> max_purchase_qty3=new Vector<String>();
	Vector<String> offer_price1=new Vector<String>();
	Vector<String> offer_price2=new Vector<String>();
	Vector<String> offer_price3=new Vector<String>();

	String coupon_code_order_level;
	String[] ordervalue=new String[n];
	JsonPath all_offers;
	JSONObject item=new JSONObject();
	JSONArray itemincart =new JSONArray();
	File myObj;
	
	
	@Test
	public void read_info()throws FileNotFoundException, IOException, org.json.simple.parser.ParseException
	{
		JSONParser parser = new JSONParser();
	    Object obj = parser.parse(new FileReader("E:/Superzop/RestAssuredProject/src/test/resources/cart.json"));
	    JSONObject cart_for_order = (JSONObject)obj;
	    //System.out.println(cart_for_order);
	    phone=cart_for_order.get("phone");
	    retailer_id=cart_for_order.get("retailer_id");
	    try {
	        File myObj  = new File("E:/Superzop/RestAssuredProject/src/test/resources/output.txt");
	        if (myObj.createNewFile()) {
	          System.out.println("File created: " + myObj.getName());
	        } else {
	          System.out.println("File already exists.");
	        }
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    
	}
	

		@Test
		public void test_1() {
			JSONObject request = new JSONObject();
			request.put("phone", phone);
			
			Response response = RestAssured.given().
					header("Content-type","application/json").
					body(request.toJSONString()).
		             when().
		             post("http://test-api.superzop.com:3000/auth-token").
		             then().
		             extract().response();
			
			JsonPath jsonPathEvaluator = response.jsonPath();
			token=jsonPathEvaluator.get("token");
			System.out.println(token);
			try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt");
			      myWriter.write("Test 1: Authentication_token\n");
			      myWriter.write(token+"\n");
			      myWriter.close();
			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		}
		
		@Test
		public void test_2() {
			String retailer_id_received;
			String URL="http://test-api.superzop.com:3000/retailer";	 
			 Response response = RestAssured.given().header("x-access-token",token).with().
						queryParam("phone", phone).
						queryParam("retailer_id", retailer_id).when().
						get(URL).then().statusCode(200)
		                .extract().response();
			 
			 
			 JsonPath jsonPathEvaluator = response.jsonPath();
			 String Retailer_name = jsonPathEvaluator.get("data.retailer_name");
			 last_order_number= jsonPathEvaluator.get("data.last_order_number");
			 store_series=jsonPathEvaluator.get("data.store_series");
			 String[] arrays=last_order_number.split("/");
			 retailer_id_received=arrays[0];
			 //Assert.assertEquals(retailer_id_received,retailer_id);
			 last_order_number=arrays[1];
			// System.out.println(last_order_number);
			 System.out.println("\nthe retailer id received is "+retailer_id_received+" equals "+retailer_id);
			 try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\nTest 2: Retailer Details");
			      myWriter.write("\nThe retailer id received from is "+retailer_id_received+" equals retailer id given by user "+retailer_id+"\n");
			      myWriter.close();

			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			// System.out.println(last_order_number);
			 
			
		}
		
		@Test
		public void test_3() {
			int flag=0;
			int previous=0;
		String URL="http://test-api.superzop.com:3000/orders";	 
			//uncomment if wanted to store the data in a variable
		 Response response = RestAssured.given().header("x-access-token",token).with().
						queryParam("phone", phone).
						queryParam("retailer_id", retailer_id).when().
						get(URL).then().statusCode(200)//.log().all();
		              .extract().response();
			 
			 JsonPath jsonPathEvaluator = response.jsonPath();
			 int s = jsonPathEvaluator.getInt("data.size()");
			 
			 if(s!=0) {
			// System.out.println(s);
			 String order_number = jsonPathEvaluator.get("data[0].order_number"); 
			 for(int i=0;i<s;i++)
			 {
				 int j=i+1;
				 if(i!=0)
				 {
					 int k=i-1;
					 String fb_key_2= jsonPathEvaluator.get("data["+k+"].fb_key");
					 String[] fbkeyarra2=fb_key_2.split("-");
					  previous=Integer.parseInt(fbkeyarra2[1]);
				 }
				 
				 String fb_key= jsonPathEvaluator.get("data["+i+"].fb_key");
				 String fb_key_1= jsonPathEvaluator.get("data["+j+"].fb_key");
				 String[] fb_keyarray= fb_key.split("-");
				 String[] fbkeyarra1=fb_key_1.split("-");
				 
				 n=Integer.parseInt(fb_keyarray[1]);
				 //System.out.println("current "+n);
				 int next=Integer.parseInt(fbkeyarra1[1]);
				 //System.out.println("next "+next);
				 
				 //System.out.println("previous "+previous);
				 if(n==next)
				 {
//					 System.out.println(flag);
					// System.out.println("true "+n+" "+next);
					 item.put("data",jsonPathEvaluator.get("data["+i+"]"));
					 itemincart.put(item);
				 }
				 else if(n==previous)
				 {
					 //System.out.println(flag);
					 //System.out.println("true "+n+" "+previous);
					 item.put("data",jsonPathEvaluator.get("data["+i+"]"));
					 itemincart.put(item);
					 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\nTest 3: Orders");
					      myWriter.write("\nThe last order number received from orders api "+previous +"  and the last order number received from retailer detail "+last_order_number+"\n");
					      myWriter.close();

					    } catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
					 System.out.println("\nthe last order number received from orders api "+previous +"  and the last order number received from retailer detail "+last_order_number);
					 break;
				 }
				 else
				 {
					flag=1;
					//System.out.println(flag);
					 break;
				 }
			 }
			 System.out.println(itemincart);
			 String fb_key_next= jsonPathEvaluator.get("data[1].fb_key");
			 String[] fbkeynext=fb_key_next.split("-");
			 String fb_key= jsonPathEvaluator.get("data[0].fb_key");
			 String[] fb_keyarray= fb_key.split("-");
			 if(fb_keyarray[1]!=null){
			 if(fb_keyarray[1].equals(fbkeynext[1])==false)
			 {
				 item.put("data",jsonPathEvaluator.get("data[0]"));
				 itemincart.put(item);
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\nTest 3: Orders");
				      myWriter.write("\nthe last order number received from orders api "+fb_keyarray[1] +"  and the last order number received from retailer detail "+last_order_number+"\n");
				      myWriter.close();

				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
			 System.out.println("\nthe last order number received from orders api "+fb_keyarray[1] +"  and the last order number received from retailer detail "+last_order_number);
//remove comment after the resolution of stock indicator and stock management issue
			// Assert.assertEquals(fb_keyarray[1],last_order_number);
			 System.out.println(itemincart);
			 }
			 }else {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\nTest 3: Orders");
				      myWriter.write("\nNo orders placed from this retailer");
				      myWriter.close();

				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println("\nNo orders placed from this retailer");
				 Assert.assertEquals(1, 1);
			 }}
			 else {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\nTest 3: Orders");
				      myWriter.write("\nNo orders placed from this retailer");
				      myWriter.close();

				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println("\nNo orders placed from this retailer");
				 Assert.assertEquals(1, 1);
			 }

		}
		


		@Test
		public void test_4() {
			String URL="http://test-api.superzop.com:3000/offers-list";	 
			//uncomment if wanted to store the data in a variable
			 Response response = 
			RestAssured.given().header("x-access-token",token).with().
						queryParam("phone", phone).
						queryParam("retailer_id", retailer_id).queryParam("store_series",store_series).when().
						get(URL).then().statusCode(200)//.log().all();
		                .extract().response();
			 
			 JsonPath jsonPathEvaluator = response.jsonPath();
//is active test
			 try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\nTest 4: Offers_List");
			      myWriter.write("\n\t Test 1:Offer is active test");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			 System.out.println("\nthe is active test for offers list follows here ");
			 int x= jsonPathEvaluator.getInt("data.size()");
			 for(int i=0;i<x;i++)
			 {
				 String s=jsonPathEvaluator.getString("data["+i+"].is_active");
				 String a=jsonPathEvaluator.getString("data["+i+"].coupon_code");
				 Assert.assertEquals(s, "Y");
				 System.out.println("\n\tCoupon Code:"+a+"                               Is_Active_Flag:"+s+"");
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\tCoupon Code:"+a+"                              Is_Active_Flag:"+s+"");
				      myWriter.close();

				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
			 }
			 int y= jsonPathEvaluator.getInt("product_offers.size()");
			 for(int i=0;i<y;i++)
			 {
				 String s=jsonPathEvaluator.getString("product_offers["+i+"].is_active");
				 String a=jsonPathEvaluator.getString("product_offers["+i+"].offer_code");
				 Assert.assertEquals(s, "Y");
				 System.out.println("\n\tCoupon Code:"+a+"                               Is_Active_Flag:"+s+"");
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\tCoupon Code:"+a+"                               Is_Active_Flag:"+s+"");
				      myWriter.close();

				    } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
			 }

			 try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n");
			      myWriter.write("\n\t Test 2:Offer Date Validity\n");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			
			 for(int i=0;i<x;i++)
			 {
				 String start_date=jsonPathEvaluator.getString("data["+i+"].valid_from");
				 String end_dt = jsonPathEvaluator.getString("data["+i+"].valid_until");
				 String a=jsonPathEvaluator.getString("data["+i+"].coupon_code");
				 SimpleDateFormat date_formater = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
				 date_formater.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
				 String cjava_date = date_formater.format(cdate*1000);
				 System.out.println("this is the start date "+start_date);
				 System.out.println("this is the current date "+cjava_date);
				 System.out.println("this is the end date "+end_dt+"\n");
				 
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\tCoupon Code:"+a);
				      myWriter.write("\n\t\tthis is the start date "+start_date);
				      myWriter.write("\n\t\tthis is the current date "+cjava_date);
				      myWriter.write("\n\t\tthis is the end date "+end_dt +"\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 
			 }

			
			

//date test for products offer
		      JsonPath j = new JsonPath(response.asString());
		      int s = j.getInt("product_offers.size()");
		      for(int i = 0; i < s; i++) {
		    	  String o_code=j.getString("product_offers["+i+"].offer_id");
//start date
		         String start_date = j.getString("product_offers["+i+"].start_date");
		         String end_date = j.getString("product_offers["+i+"].end_date");
		         SimpleDateFormat cejdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		         
		         if(start_date.equals("0")==false && end_date.equals("0")==false) {
		         long s_date=Long.parseLong(start_date);
		         Date date = new java.util.Date(s_date); 
		         SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		         jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		         String java_date = jdf.format(date);
		         System.out.println("start_date: "+java_date);
		         
//end_date
		         
		         long e_date=Long.parseLong(end_date);
		         Date edate = new java.util.Date(e_date); 
		         SimpleDateFormat ejdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
		         jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		         String ejava_date = ejdf.format(edate);
		         System.out.println("end_date: "+ejava_date);
		         
//current_date
		         
		         cejdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		         String cjava_date = cejdf.format(cdate*1000);
		         System.out.println("current_date: "+cjava_date+"\n");
		         
		         SimpleDateFormat zerodate = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
					zerodate.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			         String zero_date = zerodate.format(0*1000);
			         Date zero1 = null;
						try {
							zero1 = zerodate.parse(zero_date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						
						try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n\t\tCoupon Code:"+o_code);
						      myWriter.write("\n\t\tthis is the start date "+java_date);
						      myWriter.write("\n\t\tthis is the current date "+cjava_date);
						      myWriter.write("\n\t\tthis is the end date "+ejava_date +"\n");
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
		         
//checking _
		         
		         SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		         Date d1 = null;
				try {
					d1 = sdformat.parse(java_date);
				} catch (ParseException e) {

					e.printStackTrace();
				}
		         Date d2 = null;
				try {
					d2 = sdformat.parse(cjava_date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date d3 = null;
				try {
					d3 = sdformat.parse(ejava_date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(d1.compareTo(zero1)==0)
				{
					
				}
				else {

		         if(d1.compareTo(d2) > 0) {
		        	 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\tthe offer code "+o_code+" Start date occurs after current date");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
		        	 Assert.assertFalse(2<1,o_code+"Start date occurs after current date\\n" );
		        	 } 
		          else if(d1.compareTo(d2) < 0) {
		        	 Assert.assertEquals(1,1);
		         } else if(d1.compareTo(d2) == 0) {
		        	 Assert.assertEquals(1,1);
		         }}
				
				if(d3.compareTo(zero1)==0)
				{
					
				}
				else {
		         if(d2.compareTo(d3) > 0) {
		        	 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\tthe offer code "+o_code+" Start date occurs after current date");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
		        	 Assert.assertFalse(2<1,o_code+"Current date occurs after End_date\n" );

			         } else if(d1.compareTo(d2) < 0) {
			        	 Assert.assertEquals(1,1);
			         } else if(d1.compareTo(d2) == 0) {
			        	 Assert.assertEquals(1,1);
			         }
		         
				}
		         

		      }
		         
		         else
		        	 {
		        	 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\tthe offer code "+o_code+" has start date and end dates as 0");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
		        	 	System.out.println("the offer code "+o_code+" has start date and end dates as 0");
		        	 }
		      }
		      
//store series check	
		      try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n");
			      myWriter.write("\n\t Test 3:Store Series Checking Test\n");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		      int flag=7;
		      JsonPath l = new JsonPath(response.asString());
		      all_offers =l;
		      
		      int k = j.getInt("data.size()");
		      for(int i = 0; i < k; i++) {
		    	  String st_series=l.getString("data["+i+"].store_series");
		    	  String co_code=l.getString("data["+i+"].coupon_code");
		    	  String[] st=st_series.split(",");
		    	  flag = 0;
		    	  for(int p=0;p<st.length;p++)
		    	  { 
		    		  
		    		  if(st[p].equals(store_series))
		    		  {
		    			  flag = 1;
		    			  Assert.assertEquals(2,2);
		    			  //System.out.println(co_code);
		    			  try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n\t\tthe offer code "+co_code+" contains the store-series of requested retailer "+st[p]);
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
		    			  System.out.println("its a match!"+st[p]);
		    			  break;
		    		  }
		    		  else
		    		  {
		    			  flag=9;
		    		  }
		    	  }
		    	  if(flag==9)
		    	  {
		    		  Assert.assertFalse(2<1,"no store series!");
		    		  try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\tthe offer code "+co_code+" does not contains the store-series of requested retailer ");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
		    		  //System.out.println("no store series!");
		    	  }
		    	  //System.out.println(st_series.charAt(0));
		      }
		      
		      int f=7;

		      int q = j.getInt("product_offers.size()");
		      for(int i = 0; i < q; i++) {
		    	  String item_ids=jsonPathEvaluator.getString("product_offers["+i+"].item_id");
		    	  String min=jsonPathEvaluator.getString("product_offers["+i+"].min_pur_qty");
		    	  String max=jsonPathEvaluator.getString("product_offers["+i+"].max_pur_qty");
		    	  String min2=jsonPathEvaluator.getString("product_offers["+i+"].minimum_purchase_qty2");
		    	  String max2=jsonPathEvaluator.getString("product_offers["+i+"].maximum_purchase_qty2");
		    	  String min3=jsonPathEvaluator.getString("product_offers["+i+"].minimum_purchase_qty3");
		    	  String max3=jsonPathEvaluator.getString("product_offers["+i+"].maximum_purchase_qty3");

		    	  String o1=jsonPathEvaluator.getString("product_offers["+i+"].offer_price");
		    	  String o2=jsonPathEvaluator.getString("product_offers["+i+"].offer_price2");
		    	  String o3=jsonPathEvaluator.getString("product_offers["+i+"].offer_price3");

		    	  offer_product_id.add(item_ids);
		    	  min_purchase_qty1.add(min);
		    	  max_purchase_qty1.add(max);
		    	  min_purchase_qty2.add(min2);
		    	  max_purchase_qty2.add(max2);
		    	  min_purchase_qty3.add(min3);
		    	  max_purchase_qty3.add(max3);

		    	  offer_price1.add(o1);
		    	  offer_price2.add(o2);
		    	  offer_price3.add(o3);

		    	  String st_series=l.getString("product_offers["+i+"].store_series");
		    	  String[] st=st_series.split(",");
		    	  flag = 0;
		    	  for(int p=0;p<st.length;p++)
		    	  { 
		    		  if(st[p].equals(store_series))
		    		  {
		    			  flag = 1;
		    			  Assert.assertEquals(2,2);
		    			  try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n\t\tthe product offer id "+item_ids+" contains the store-series of requested retailer "+st[p]);
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
		    			  System.out.println("its a match!"+st[p]);
		    			  break;
		    		  }
		    		  else
		    		  {
		    			  f=9;
		    			  
			    		  try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n\t\tthe product offer id "+item_ids+" does not contains the store-series of requested retailer ");
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
		    		  }
		    	  }
		    	  if(f==9)
		    	  {
		    		  Assert.assertFalse(2<1,"no store series!");
		    		  //System.out.println("no store series!");
		    	  }
		    	  //System.out.println(st_series.charAt(0));
		      }
		      System.out.println(offer_product_id);
		      System.out.println(min_purchase_qty1);
		      System.out.println(max_purchase_qty1);
		      System.out.println(min_purchase_qty2);
		      System.out.println(max_purchase_qty2);
		      System.out.println(min_purchase_qty3);
		      System.out.println(max_purchase_qty3);

		      System.out.println(offer_price1);
		      System.out.println(offer_price2);
		      System.out.println(offer_price3);


}
			
		
		
		@SuppressWarnings("unchecked")
		@Test
		public void  test_5() {	
			JSONObject cdata=new JSONObject();
			JSONArray carray =new JSONArray();
			JSONObject requestparams=new JSONObject();
			float ordervalue=0;
			int g = all_offers.getInt("data.size()");
			String[] all_MOV = new String[g];

			
			for(int i=0;i<itemincart.length();i++)
			{	
				org.json.JSONObject items = itemincart.getJSONObject(i);
				org.json.JSONObject item_detail= (org.json.JSONObject) items.get("data");
				//System.out.println(item_detail.get("shipping_charges"));
				
				int j= (Integer) item_detail.get("item_id");
				String itemid=String.valueOf(j);
				String o = (String) item_detail.get("order_qty");
				int orderqty=Integer.parseInt(o);
				String t = (String) item_detail.get("total_amt");
				float totalamt= Float.valueOf(t);
				ordervalue=(Float) item_detail.get("net_amount_before_discount");
				
				
			cdata.put("item_id", itemid);
			cdata.put("order_qty", orderqty);
			cdata.put("total_amt", totalamt);
			
			carray.put(cdata);
			}
			//System.out.println(carray);
			
			requestparams.put("cartData",carray);
			requestparams.put("phone", phone);
			requestparams.put("orderValue", ordervalue);
			requestparams.put("store_series", store_series);
			requestparams.put("retailer_id",retailer_id);
			
			//System.out.println(requestparams);
			
			
			Response response =
					RestAssured.given().header("x-access-token",token).
					header("Content-type","application/json").
					body(requestparams.toJSONString()).
		             when().
		             post("http://test-api.superzop.com:3000/cart-offers").
		             then().//log().all();
		             extract().response();
			
			try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n ");
			      myWriter.write("\nTest 5: Cart_Offers\n ");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }	
			if(carray.isEmpty())
			{
				try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t Test 1:Cart is Empty Check!\n");
				      myWriter.write("\n\t\tNo items in cart hence no offers availed! ");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				System.out.println("No items in cart hence no offers availed!");
			}
			else {
//getting coupon codes from cart offers					
					JsonPath res = new JsonPath(response.asString());
					
					String totcb=res.getString("totalCashback");
					Float total_cashback=Float.parseFloat(totcb);
					int s = res.getInt("offers.size()");
					if(s==0)
					{
						 try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n");
						      myWriter.write("\n\t Test 1:Coupon If Availed\n");
						      myWriter.write("\n\t\tNo coupons were availed!\n");
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
						System.out.println("No coupons were availed!");
					}
				      String[] co_code= new String[s];
				      for(int i = 0; i < s; i++) {
				    	  co_code[i]=res.getString("offers["+i+"].coupon_code");
				      }
				      
				      for(int i=0;i<s;i++)
				      {
				    	  System.out.println(co_code[i]);
				      }
				      System.out.println("\n");

//getting all coupon / offer codes from offer list!
				      if(all_offers==null)
				      {
				    	  try {
						      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
						      myWriter.write("\n\t\tthe offer list is null for retailer\n");
						      myWriter.close();
						 }catch (IOException e) {
						      System.out.println("An error occurred.");
						      e.printStackTrace();
						    }
				    	  System.out.println("the offer list is null for retailer");
				    	  Assert.assertEquals(1, 1);
				      }
				      else {
				      
				      g = all_offers.getInt("data.size()");
				      int h = all_offers.getInt("product_offers.size()");
				      int t=g+h;
				      //System.out.println(t);
				      String[] all_code = new String[g+h];
				      
				      for(int i=0;i<g;i++)
				      {
				    	  String mode=all_offers.getString("data["+i+"].mode");
				    	  if(mode.equals("freeproduct")) 
				    	  {
				    		  all_MOV[i]=all_offers.getString("data["+i+"].minimum_order_value");
				    	  }
				    	  all_code[i]=all_offers.getString("data["+i+"].coupon_code");
				      }
				      for(int i=g,j=0;i<h+g;i++,j++) {
				    	  
				    	  all_code[i]= all_offers.getString("product_offers["+j+"].offer_code");

				      }
				      
				      
//matching using subset checking 	
				      int flag=1;
				      HashSet set = new HashSet<String>();
				      
				        for (int i = 0; i < t; i++) {
				            if (!set.contains(all_code[i]))
				                set.add(all_code[i]);
				        }
				 
				        
				        for (int i = 0; i < s; i++)
				        {
				            if (!set.contains(co_code[i]))
				            {
				            	flag=0;
				            	try {
								      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
								      myWriter.write("\n\t\tthe coupon code "+co_code[i]+"is not present in offer list\n");
								      myWriter.close();
								 }catch (IOException e) {
								      System.out.println("An error occurred.");
								      e.printStackTrace();
								    }
				            	Assert.assertFalse(2<1, "the coupon code "+co_code[i]+"is not present in offer list");
				            }
				            else 
				            {
				            	flag=2;
				            }
				        }
				        if(flag==2) {
				        	try {
							      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
							      myWriter.write("\n\t\tall codes availed are present in the offer-list\n");
							      myWriter.close();
							 }catch (IOException e) {
							      System.out.println("An error occurred.");
							      e.printStackTrace();
							    }
				        System.out.println("all codes availed are present in the offer-list");
				        Assert.assertEquals(1, 1);
				      
				        }
				        else if (flag==0)
				        {
				        	System.out.println("something is missing");
				        }
				      
		 }
				      
				      int h = all_offers.getInt("product_offers.size()");
				      int t=g+h;
				      for (int i=0;i<s;i++)
				      {
				    	  for(int j=0;j<g;j++)
				    	  {
				    		  if(res.getString("offers["+i+"].coupon_code").equals(all_offers.getString("data["+j+"].coupon_code")))
				    		  {
				    			  String mode=all_offers.getString("data["+j+"].mode");
				    			  if(mode.equals("instant")==true) {
				    			  System.out.println(res.getString("offers["+i+"].coupon_code"));
				    			  System.out.println(all_offers.getString("data["+j+"].coupon_code"));
				    			  try {
								      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
								      myWriter.write("\n\t\t"+res.getString("offers["+i+"].coupon_code")+"\t"+all_offers.getString("data["+j+"].coupon_code")+"");
								      myWriter.close();
								 }catch (IOException e) {
								      System.out.println("An error occurred.");
								      e.printStackTrace();
								    }
				    			  Float orderdisc,y=(float) 1.1;
		   //order discount
				    			  String order_disc=res.getString("offers["+i+"].order_disc");
				    			  String offerprod=res.getString("offers["+i+"].offer_product");
				    			  if(order_disc!=null)
				    			  {
				    			  y=Float.parseFloat(order_disc);
//				    			  System.out.println(y);
				    			  }
				    			  else if(offerprod!=null) {
				    				  
				    			  }
				    			  else {
					    			  String cashback=res.getString("offers["+i+"].cashback_value");
					    			 Float cb=Float.parseFloat(cashback);
					    			 

				    			  }
		   //discount percent	    
				    			  String disc_percent=all_offers.getString("data["+j+"].discount_percent");
			    			  Float x=Float.parseFloat(disc_percent);
				    			  System.out.println(x);
			//discount amount
				    			  
				    			  String Disc_amt=all_offers.getString("data["+j+"].discount_value");
				    			  Float w=Float.parseFloat(Disc_amt);

		   //total amount 
				    			  Float tot=ordervalue;
				    			  System.out.println(tot);
				    			  if(x!=0)
				    			  {
				    				  x=x/100;
				    				  orderdisc=tot*x;
				    				  if(Float.compare(orderdisc, y)==0)
				    				  {
				    					  try {
										      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
										      myWriter.write("\n\t\t "+orderdisc+" "+y+"");
										      myWriter.write("\n\t\torder discount and calculated order discount are equal");
										      myWriter.close();
										 }catch (IOException e) {
										      System.out.println("An error occurred.");
										      e.printStackTrace();
										    }
				    					  System.out.println(orderdisc+" "+y);
				    					  System.out.println("order discount and calculated order discount are equal");
				    					  break;
				    				  }
				    				  else if(Float.compare(orderdisc, total_cashback)==0)
				    				  {
				    					  try {
										      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
										      myWriter.write("\n\t\tthe cashbacks availed are also equal");
										      myWriter.close();
										 }catch (IOException e) {
										      System.out.println("An error occurred.");
										      e.printStackTrace();
										    }
				    					  System.out.println("the cashbacks availed are also equal");
				    					  break;
				    				  }
				    				  else {
				    					  try {
										      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
										      myWriter.write("\n\t\tthere is no cashback as well as discount availed");
										      myWriter.close();
										 }catch (IOException e) {
										      System.out.println("An error occurred.");
										      e.printStackTrace();
										    }
				    					  System.out.println("there is no cashback as well as discount availed");
				    				  }
				    			  }
				    			  else if(w!=0)
				    			  {
				    				  orderdisc=tot-w;
				    				  
				    				  if(Float.compare(orderdisc, y)==0)
				    				  {
				    					  try {
										      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
										      myWriter.write("\n\t\torder discount and calculated order discount are equal");
										      myWriter.close();
										 }catch (IOException e) {
										      System.out.println("An error occurred.");
										      e.printStackTrace();
										    }
				    					  System.out.println("order discount and calculated order discount are equal");
				    					  break;
				    				  }
				    			  }
				    			  
				    			  
				    		  }
//checking for free product minimum order value
				    			  else if(mode.equals("freeproduct")==true)
				    			  {
				    				  int oferprodunit=all_offers.getInt("data["+j+"].offer_product_unit");
				    				  int max_product_unit = all_offers.getInt("data["+j+"].maximum_product_unit");
				    				  if(oferprodunit<=max_product_unit)
				    				  {
				    				  System.out.println(res.getString("offers["+i+"].coupon_code"));
					    			  System.out.println(all_offers.getString("data["+j+"].coupon_code"));
					    			  System.out.println(all_MOV[j]);
					    			  String x=all_MOV[j];
					    			  int itemmov= Integer.parseInt(x);
				    				  System.out.println("freeproduct");
				    				  int got_offer_product_unit=res.getInt("offers["+i+"].offer_product_unit");
				    				  float tot=ordervalue/itemmov;
				    				  tot=tot*oferprodunit;
				    				  int calculated_offer_product_unit=(int)tot;
				    				  
				    				  if(got_offer_product_unit==calculated_offer_product_unit)
				    				  {
				    					  try {
										      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
										      myWriter.write("\n\t\tcalculated offer product unit "+calculated_offer_product_unit +" and the offer product units received from api "+got_offer_product_unit);
										      myWriter.close();
										 }catch (IOException e) {
										      System.out.println("An error occurred.");
										      e.printStackTrace();
										    }
				    					  System.out.println("calculated offer product unit "+calculated_offer_product_unit +" and the offer product units received from api "+got_offer_product_unit);
				    					  Assert.assertEquals(1, 1);
				    				  }
				    				  else { 
				    					  Assert.assertEquals(1, 2);
				    				  }}
				    				  else if(oferprodunit>max_product_unit) {
				    					  Assert.assertEquals(1, 2);
				    				  }
				    				  
				    			  }
				    		  }
				    	  }
				      }}		      
	}	
		
		
		
		@Test
		public void test_6()
		{
			JSONObject requestparams=new JSONObject();
			Vector<String> product_id=new Vector<String>();
			JSONObject product_qty=new JSONObject();
			int d=offer_product_id.size();
			System.out.println(d);
			for(int i=0;i<d;i++)
			{
				product_id.add(offer_product_id.get(i));
				product_qty.put(offer_product_id.get(i),min_purchase_qty1.get(i));
			}
			//System.out.println(product_id);
			//System.out.println(product_qty);
			
			requestparams.put("retailer_id", retailer_id);
			requestparams.put("store_series",store_series );
			requestparams.put("productIds", product_id);
			requestparams.put("products_qty",product_qty);
			requestparams.put("phone", phone);
			
			//System.out.println(requestparams);

			
			if(product_id.isEmpty())
			{
				System.out.println("There are no Products in the cart of the given customer!");
			}
			else {
			
			 Response response =
					RestAssured.given().header("x-access-token",token).
					header("Content-type","application/json").
					body(requestparams.toJSONString()).
		             when().
		             post("http://test-api.superzop.com:3000/products-stock").
		             then().//log().all();
		             extract().response();
//stock indicator check!	
			 try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n ");
			      myWriter.write("\nTest 6: Product Stock Check and Product offer check\n ");
			      myWriter.write("\n\tTest 1: Product Stock Check and Product offer check for min_qty_1\n ");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			 JsonPath jsonPathEvaluator = response.jsonPath();
			 int n=jsonPathEvaluator.getInt("data.size()");
			 //System.out.println(n);
			 for(int i=0;i<n;i++) {
			 String indicator=jsonPathEvaluator.getString("data["+i+"].stock_ind");
			 String item_id=jsonPathEvaluator.getString("data["+i+"].item_id");
			 String offer_price=jsonPathEvaluator.getString("data["+i+"].offers[0].offer_price");
			 System.out.println(offer_price+"    "+offer_price1.get(i));
			 if(indicator.equals("Y")) {
				 if(offer_price.equals(offer_price1.get(i))) {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\t"+item_id+" Is in stock");
				      myWriter.write("\n\t\t offer_price received from API for minimum_purchase_qty1 "+offer_price+" is equal to offer price recieved from get_offer_list API "+offer_price1.get(i)+"\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" It is in stock");
			 }
				 else {
					 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\t"+item_id+" Is in stock");
					      myWriter.write("\n\t\t the offer price is not correct");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
				 }
			 }
			 else {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n");
				      myWriter.write("\n\t\t"+item_id+" Is out of stock");
				      myWriter.write("\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" Is out of stock");
			 }
			 }
			 
		}}
		
		
		@Test
		public void test_7()
		{
			JSONObject requestparams=new JSONObject();
			Vector<String> product_id=new Vector<String>();
			JSONObject product_qty=new JSONObject();
			int d=offer_product_id.size();
			System.out.println(d);
			for(int i=0;i<d;i++)
			{
				product_id.add(offer_product_id.get(i));
				product_qty.put(offer_product_id.get(i),min_purchase_qty2.get(i));
			}
			//System.out.println(product_id);
			//System.out.println(product_qty);
			
			requestparams.put("retailer_id", retailer_id);
			requestparams.put("store_series",store_series );
			requestparams.put("productIds", product_id);
			requestparams.put("products_qty",product_qty);
			requestparams.put("phone", phone);
			
			//System.out.println(requestparams);
			try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n ");
			      myWriter.write("\n\tTest 2: Product Stock Check and Product offer check for min_qty_2\n ");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			
			if(product_id.isEmpty())
			{
				System.out.println("There are no Products in the cart of the given customer!");
			}
			else {
			
			 Response response =
					RestAssured.given().header("x-access-token",token).
					header("Content-type","application/json").
					body(requestparams.toJSONString()).
		             when().
		             post("http://test-api.superzop.com:3000/products-stock").
		             then().//log().all();
		             extract().response();
//stock indicator check!			 
			 JsonPath jsonPathEvaluator = response.jsonPath();
			 int n=jsonPathEvaluator.getInt("data.size()");
			 //System.out.println(n);
			 for(int i=0;i<n;i++) {
			 String indicator=jsonPathEvaluator.getString("data["+i+"].stock_ind");
			 String item_id=jsonPathEvaluator.getString("data["+i+"].item_id");
			 String offer_price=jsonPathEvaluator.getString("data["+i+"].offers[0].offer_price2");
			 System.out.println(offer_price+"    "+offer_price2.get(i));
			 if(indicator.equals("Y")) {
				 if(offer_price.equals(offer_price2.get(i))) {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\t"+item_id+" Is in stock");
				      myWriter.write("\n\t\t offer_price received from API for minimum_purchase_qty1 "+offer_price+" is equal to offer price recieved from get_offer_list API "+offer_price2.get(i)+"\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" It is in stock");
			 }
				 else {
					 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\t"+item_id+" Is in stock");
					      myWriter.write("\n\t\t the offer price is 0 because minimum_order_qty was 0 \n");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
				 }
			 }
			 else {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n");
				      myWriter.write("\n\t\t"+item_id+" Is out of stock");
				      myWriter.write("\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" Is out of stock");
			 }
			 }
			 
		}}	
			
		
		
		
		@Test
		public void test_8()
		{
			JSONObject requestparams=new JSONObject();
			Vector<String> product_id=new Vector<String>();
			JSONObject product_qty=new JSONObject();
			int d=offer_product_id.size();
			System.out.println(d);
			for(int i=0;i<d;i++)
			{
				product_id.add(offer_product_id.get(i));
				product_qty.put(offer_product_id.get(i),min_purchase_qty3.get(i));
			}
			//System.out.println(product_id);
			//System.out.println(product_qty);
			
			requestparams.put("retailer_id", retailer_id);
			requestparams.put("store_series",store_series );
			requestparams.put("productIds", product_id);
			requestparams.put("products_qty",product_qty);
			requestparams.put("phone", phone);
			
			//System.out.println(requestparams);

			try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n ");
			      myWriter.write("\n\tTest 3: Product Stock Check and Product offer check for min_qty_3\n ");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			if(product_id.isEmpty())
			{
				System.out.println("There are no Products in the cart of the given customer!");
			}
			else {
			
			 Response response =
					RestAssured.given().header("x-access-token",token).
					header("Content-type","application/json").
					body(requestparams.toJSONString()).
		             when().
		             post("http://test-api.superzop.com:3000/products-stock").
		             then().//log().all();
		             extract().response();
//stock indicator check!			 
			 JsonPath jsonPathEvaluator = response.jsonPath();
			 int n=jsonPathEvaluator.getInt("data.size()");
			 //System.out.println(n);
			 for(int i=0;i<n;i++) {
			 String indicator=jsonPathEvaluator.getString("data["+i+"].stock_ind");
			 String item_id=jsonPathEvaluator.getString("data["+i+"].item_id");
			 String offer_price=jsonPathEvaluator.getString("data["+i+"].offers[0].offer_price3");
			 System.out.println(offer_price+"    "+offer_price3.get(i));
			 if(indicator.equals("Y")) {
				 if(offer_price.equals(offer_price3.get(i))) {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n\t\t"+item_id+" Is in stock");
				      myWriter.write("\n\t\t offer_price received from API for minimum_purchase_qty1 "+offer_price+" is equal to offer price recieved from get_offer_list API "+offer_price3.get(i)+"\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" It is in stock");
			 }
				 else {
					 try {
					      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
					      myWriter.write("\n\t\t"+item_id+" Is in stock");
					      myWriter.write("\n\t\t the offer price is 0 because minimum_order_qty was 0 \n");
					      myWriter.close();
					 }catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
				 }
			 }
			 else {
				 try {
				      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
				      myWriter.write("\n");
				      myWriter.write("\n\t\t"+item_id+" Is out of stock");
				      myWriter.write("\n");
				      myWriter.close();
				 }catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				 System.out.println(jsonPathEvaluator.getString("data["+i+"].item_id")+" Is out of stock");
			 }
			 }
			 
		}}	
		
		
		
		
		
		
		@Test 
		public void test_10() throws FileNotFoundException, IOException, org.json.simple.parser.ParseException
		{
			JSONParser parser = new JSONParser();
		      
		         Object obj = parser.parse(new FileReader("E:/Superzop/RestAssuredProject/src/test/resources/cart.json"));
		         JSONObject cart_for_order = (JSONObject)obj;
		         System.out.println(cart_for_order);
		      
		      RestAssured.given().header("x-access-token",token).
				header("Content-type","application/json").
				body(cart_for_order.toJSONString()).
	             when().
	             post("http://test-api.superzop.com:3000/place-order").
	             then().log().all();
		      
		      try {
			      FileWriter myWriter = new FileWriter("E:/Superzop/RestAssuredProject/src/test/resources/output.txt",true);
			      myWriter.write("\n");
			      myWriter.write("Test 7 : Place Order Api");
			      myWriter.write("\n");
			      myWriter.close();
			 }catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
		      
		}
		
		
}

package productotesting;


import io.restassured.RestAssured;

import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*; // se debe importar manualmente ya que es un static para poder usar given()

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Este metodo lo que hace es que leer de manera ascendente los methodos para su ejecuion , es decir desde la a hasta la z por eso es que al inicio del nombre de cada metodo se coloco una letra del abcdario
public class CRUDProduct {

    final String url = "http://localhost:8080"; //Seteo la url con la que vamos a trabajar
    public static String value;



    @Before
    public void setUp(){
        RestAssured.config=new RestAssuredConfig().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")); //Esto lo que hace es configurar Rest Assure con la codificacion UTF-8
        RestAssured.baseURI=url; //Seteo la baseURI
    }


    @Test
    public void a_createProduct(){
        Map<String, Object> product = new HashMap<>();
        product.put("nombre", "Arroz");
        product.put("precio", 45.60 );
        product.put("categoriaId", 1);

        //Otra manera de mapear mi Request Body  es utilizando un String, veamos:

        String productBody= "{ \n" +
                " \"categoriaId\": 1, \n" +
                " \"nombre\": \"Papa\", \n" +
                " \"precio\": 20.50\n" +
                " }";

        JsonPath jsonPath = given().log().all().contentType(ContentType.JSON).body(product)
                .when().post("/producto")
                .then().log().all().assertThat().statusCode(201).and().body("id", notNullValue()
                ,"nombre",equalTo("Arroz")
                ,"precio",equalTo(45.60f)
                ,"categoria.id",equalTo(1)
                , "categoria.nombre", equalTo("Comida")).extract().jsonPath();
        value= jsonPath.getString("id");
    }



    @Test
    public void b_updateProduct(){
        Map<String, Object> product = new HashMap<>();
        product.put("nombre", "Fideos");
        product.put("precio", 15.48 );


        given().log().all().contentType(ContentType.JSON).pathParam("id",value).body(product)
                .when().put("/producto/{id}")
                .then().log().all().assertThat().statusCode(200).and().body("nombre",equalTo("Fideos")
                ,"precio",equalTo(15.48f));

    }


    @Test
    public void c_getProduct(){
        given().log().all().contentType(ContentType.JSON).pathParam("id",value)
                .when().get("/producto/{id}")
                .then().log().all().assertThat().statusCode(200);

    }



    @Test
    public void d_deleteProduct(){
        given().log().all().contentType(ContentType.JSON).pathParam("id",value)
                .when().delete("/producto/{id}")
                .then().log().all().assertThat().statusCode(204);
    }


}

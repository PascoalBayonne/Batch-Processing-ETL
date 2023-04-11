package pt.com.bayonnesensei.salesInfo.batch;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SalesInfoTestDataProviderUtils {

    public static String supplyValidContent(){
        return """
                Product,Seller,Seller ID,Price,City,Category
                Fuji 5.2GB DVD-RAM,Jack Garza,2631,40.96,Nunavut,Computer Peripherals
                Bevis Steel Folding Chairs,Julia West,2757,95.95,Nunavut,Chairs & Chairmats
                Avery Binder Labels,Eugene Barchas,2791,3.89,Nunavut,Binders and Binder Accessories
                Hon Every-Day® Chair Series Swivel Task Chairs,Eugene Barchas,2791,120.98,Nunavut,Chairs & Chairmats
                """;
    }

    public static String supplyInvalidContent(){
        return """
                firstname,lastname,email
                john,wick,boogieman@hotmail.com
                josh,long,spring@thebest.com
                nasir,jones,goathiphop@gmail.com
                mahmoud,ben hassine,someonegood@gmail.com
                franz,kafka,karafka@setup.com
                john,wick,boogieman@hotmail.com
                josh,long,spring@thebest.com
                mahmoud,ben hassine,someonegood@gmail.com
                franz,kafka,karafka@setup.com
                john,wick,boogieman@hotmail.com
                josh,long,spring@thebest.com
                mahmoud,ben hassine,someonegood@gmail.com
                franz,kafka,karafka@setup.com
                """;
    }
}

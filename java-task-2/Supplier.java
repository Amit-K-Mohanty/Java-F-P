import java.util.function.Supplier;

class D_SupplierTest{
     public void d_supplier1(){
         Supplier<String> sup = () -> "abc";
         assert "abc".equals(sup.get());
     }
    
}

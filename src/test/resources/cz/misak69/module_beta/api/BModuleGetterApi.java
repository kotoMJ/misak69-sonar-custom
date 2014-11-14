package cz.misak69.module_beta.api;




import java.util.ArrayList;
import java.util.List;

public class BModuleGetterApi {

    public static String MODULE_B_API = "Api of module B";

    private boolean innerFlag;

    public boolean isInnerFlag() {
        return innerFlag;
    }

    public void setInnerFlag(boolean innerFlag) {
        this.innerFlag = innerFlag;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private class BModuleGetterApiInner {
        private boolean innerFlag;

        public boolean isInnerFlag() {
            return this.innerFlag;
        }

        public boolean getInnerFlag() {
            return this.innerFlag;
        }

        public void setInnerFlag(boolean innerFlag) {
            this.innerFlag = innerFlag;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public boolean getInnerFlag() {
        return innerFlag;
    }
    class Label {
        public Label(String str1,String str2){

        }
    }

    class Dummy {

    }

    public void sameInnerClasses() {
        /**
         *  deti do 10 roku
         */
        Object o = new Label("childrenTo10Years", "creditCardRequestDetail.numberOfChildrenTo10Years") {

            public boolean isVisible() {
                return true;
            }

            public boolean getVisible() {
                return false;
            }
        };

        /**
         *  11 - 25 rokov
         */
        o = new Label("childrenFrom11To25Years", "creditCardRequestDetail.numberOfChildrenFrom11To25Years") {

            public boolean isVisible() {
                return false;
            }
        };
    }

    public void lastMethodFromBModuleGetterApi(){

    }

    public class CurrentAdvicesPanel extends BModuleGetterApiInner {
        private List<Object> getAdviceView(int i,final List<Object> list){
            return new ArrayList<Object>(){

            };
        };
    }

    public class BookedAdvicesPanel extends BModuleGetterApiInner {
        private List<Object> getAdviceView(int i,final List<Object> list){
            return new ArrayList<Object>(){

            };
        };
    }

    public static class SubProduct {
        public SubProduct(String contractNumber, String productName) {
        }

        public String getContractNumber() {
            return null;
        }
    }

    public String getContractNumber() {
        return null;
    }


}

package com.example.yilaoapp.ui.purchase;

import com.example.yilaoapp.R;

public class Purchase {
        private String objectName;
        private String content;
        private String money;
        private String isPurchase;
        private int[] imageId;

        public Purchase(String objectName, String content, String money, String isPurchase, int []imageId) {
            this.objectName = objectName;
            this.content = content;
            this.money = money;
            this.isPurchase = isPurchase;
            this.imageId = imageId;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getIsPurchase() {
            return isPurchase;
        }

        public void setIsPurchase(String isPurchase) {
            this.isPurchase = isPurchase;
        }

        public int[] getImageId() {
            return imageId;
        }

        //public void setImageId(int []imageId) {
            //this.imageId = imageId;

       // }
       public void setImageId()
       {
           for(int i=0;i<3;i++)
           {
               this.imageId[i]= R.drawable.kouhong;
           }
       }
}

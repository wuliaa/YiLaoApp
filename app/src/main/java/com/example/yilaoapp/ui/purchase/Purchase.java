package com.example.yilaoapp.ui.purchase;

import com.example.yilaoapp.R;

import java.util.ArrayList;

public class Purchase {
        private String objectName;
        private String content;
        private String money;
        private String isPurchase;
        private ArrayList<String> photos;
        private int imageid;

        public Purchase(String objectName, String content, String money, String isPurchase,
                        ArrayList<String> photos,int imageid) {
            this.objectName = objectName;
            this.content = content;
            this.money = money;
            this.isPurchase = isPurchase;
            this.photos = photos;
            this.imageid=imageid;
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

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<String> photos) {
            this.photos = photos;
        }

        public int getImageid() {
            return imageid;
        }

        public void setImageid(int imageid) {
            this.imageid = imageid;
        }
}

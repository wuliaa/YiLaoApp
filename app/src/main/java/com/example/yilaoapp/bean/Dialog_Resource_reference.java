package com.example.yilaoapp.bean;

//对话与资源关联
public class Dialog_Resource_reference {
    private int dialog; //dialog_id
    private int resource;  //resource_id

    public Dialog_Resource_reference(int dialog, int resource) {
        this.dialog = dialog;
        this.resource = resource;
    }

    public int getDialog() {
        return dialog;
    }

    public void setDialog(int dialog) {
        this.dialog = dialog;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}

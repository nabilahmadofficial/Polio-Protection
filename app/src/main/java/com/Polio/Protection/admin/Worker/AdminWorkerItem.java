package com.Polio.Protection.admin.Worker;

public class AdminWorkerItem {

    String worker_key, worker_name, worker_gender;
    int worker_no;

    public AdminWorkerItem(String worker_key, String worker_name, String worker_gender, int worker_no) {
        this.worker_key = worker_key;
        this.worker_name = worker_name;
        this.worker_gender = worker_gender;
        this.worker_no = worker_no;
    }

    public String getWorker_key() {
        return worker_key;
    }

    public void setWorker_key(String worker_key) {
        this.worker_key = worker_key;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getWorker_gender() {
        return worker_gender;
    }

    public void setWorker_gender(String worker_gender) {
        this.worker_gender = worker_gender;
    }

    public int getWorker_no() {
        return worker_no;
    }

    public void setWorker_no(int worker_no) {
        this.worker_no = worker_no;
    }
}

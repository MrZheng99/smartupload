package com.zj.demo.entity;

public class Student {
	private Integer sid;

	private String sname;
	private String address;
	private String birth;
	private Integer age;
	private String tel;
	private Integer cid;
	private String photo;
	private String cname;

	public Student() {

	}

	public Student(String sname, String address, String birth, Integer age, String tel, Integer cid, String photo) {
		super();
		this.sname = sname;
		this.address = address;
		this.birth = birth;
		this.age = age;
		this.tel = tel;
		this.cid = cid;
		this.photo = photo;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Student [sid=" + sid + ", sname=" + sname + ", address=" + address + ", birth=" + birth + ", age=" + age
				+ ", tel=" + tel + ", cid=" + cid + ", photo=" + photo + ", cname=" + cname + "]";
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

}

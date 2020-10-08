package de.ocplearn.hv.model;

public class Contact implements Comparable<Contact>{

	private long id;
	private String sex;
	private String firstName;
	private String lastName;
	private boolean isCompany;
	private String compayName;
	private String phone;
	private String mobilePhone;
	private String fax;
	private String website;
	private String email;
	
	public static class ContactBuilder{
		
		// Artikel zum Thema: https://dzone.com/articles/design-patterns-the-builder-pattern
		
		private long id;
		private String sex;
		private String firstName;
		private String lastName;
		private boolean isCompany;
		private String compayName;
		private String phone;
		private String mobilePhone;
		private String fax;
		private String website;
		private String email;
		
		public ContactBuilder() {
			
		}
		
		public Contact build() {
			Contact contact = new Contact();
			contact.id=id;
			contact.sex = sex;
			contact.firstName = firstName;
			contact.lastName = lastName;
			contact.isCompany = isCompany;
			contact.compayName = compayName;
			contact.phone = phone;
			contact.mobilePhone = mobilePhone;
			contact.fax = fax;
			contact.website = website;
			contact.email = email;
			
			return contact;
		}
		
	
		
		public ContactBuilder setSex(String sex) {
			this.sex=sex;
			return this;
		}
		
		

		/**
		 * @param id the id to set
		 */
		public ContactBuilder setId(long id) {
			this.id = id;
			return this;
		}

		/**
		 * @param firstName the firstName to set
		 */
		public ContactBuilder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		/**
		 * @param lastName the lastName to set
		 */
		public ContactBuilder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		/**
		 * @param isCompany the isCompany to set
		 */
		public ContactBuilder setCompany(boolean isCompany) {
			this.isCompany = isCompany;
			return this;
		}

		/**
		 * @param compayName the compayName to set
		 */
		public ContactBuilder setCompayName(String compayName) {
			this.compayName = compayName;
			return this;
		}

		/**
		 * @param phone the phone to set
		 */
		public ContactBuilder setPhone(String phone) {
			this.phone = phone;
			return this;
		}

		/**
		 * @param mobilePhone the mobilePhone to set
		 */
		public ContactBuilder setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
			return this;
		}

		/**
		 * @param fax the fax to set
		 */
		public ContactBuilder setFax(String fax) {
			this.fax = fax;
			return this;
		}

		/**
		 * @param website the website to set
		 */
		public ContactBuilder setWebsite(String website) {
			this.website = website;
			return this;
		}

		/**
		 * @param email the email to set
		 */
		public ContactBuilder setEmail(String email) {
			this.email = email;
			return this;
		}
		
		
	}
	
	
	public Contact() {
		
	}
	
	public Contact(String sex, String firstName, String lastName, boolean isCompany, String compayName, String phone,
			String mobilePhone, String fax, String website, String email) {
		super();
		this.sex = sex;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isCompany = isCompany;
		this.compayName = compayName;
		this.phone = phone;
		this.mobilePhone = mobilePhone;
		this.fax = fax;
		this.website = website;
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isCompany() {
		return isCompany;
	}
	public void setCompany(boolean isCompany) {
		this.isCompany = isCompany;
	}
	public String getCompayName() {
		return compayName;
	}
	public void setCompayName(String compayName) {
		this.compayName = compayName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compayName == null) ? 0 : compayName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Contact [sex=" + sex + ", firstName=" + firstName + ", lastName=" + lastName + ", isCompany="
				+ isCompany + ", compayName=" + compayName + ", phone=" + phone + ", mobilePhone=" + mobilePhone
				+ ", fax=" + fax + ", website=" + website + ", email=" + email + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;

		if (id != other.id)
			return false;
	
		return true;
	}

	@Override
	public int compareTo(Contact o) {
		if(isCompany()) {
			return this.getCompayName().compareTo(o.getCompayName());
		}else {
			if(this.getLastName().equalsIgnoreCase(o.getLastName())){
				//Vergleiche firstName
				return this.getFirstName().compareTo(o.getFirstName());
			
			}else {
				return this.getLastName().compareTo(o.getLastName());
			}
		}
	
	}
	
	
	
}

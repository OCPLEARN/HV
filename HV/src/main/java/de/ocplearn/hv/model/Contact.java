package de.ocplearn.hv.model;

import java.util.List;

public class Contact implements Comparable<Contact>{

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	private int id;
	private String sex;
	private String firstName;
	private String lastName;
	private boolean isCompany;
	private String companyName;
	private String phone;
	private String mobilePhone;
	private String fax;
	private String website;
	private String email;
	private List<Address> addresses;
	
	public static class ContactBuilder{
		
		// Artikel zum Thema: https://dzone.com/articles/design-patterns-the-builder-pattern
		
		private int id;
		private String sex;
		private String firstName;
		private String lastName;
		private boolean isCompany;
		private String companyName;
		private String phone;
		private String mobilePhone;
		private String fax;
		private String website;
		private String email;
		private List<Address> addresses;
		
		
		
		public ContactBuilder() {
			
		}
		
		public Contact build() {
			Contact contact = new Contact();
			contact.id=id;
			contact.sex = sex;
			contact.firstName = firstName;
			contact.lastName = lastName;
			contact.isCompany = isCompany;
			contact.companyName = companyName;
			contact.phone = phone;
			contact.mobilePhone = mobilePhone;
			contact.fax = fax;
			contact.website = website;
			contact.email = email;
			contact.addresses = addresses;
			
			return contact;
		}
		
	
		
		public ContactBuilder setSex(String sex) {
			this.sex=sex;
			return this;
		}
		
		

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public ContactBuilder setId(int id) {
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
		 * @param companyName the companyName to set
		 */
		public ContactBuilder setCompanyName(String companyName) {
			this.companyName = companyName;
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
		
		/**
		 * @param List<Address> the addresses to set
		 */
		public ContactBuilder setAddressList(List<Address> addresses) {
			this.addresses = addresses;
			return this;
		}

		
		
		
	}
	
	
	public Contact() {
		
	}
	
	public Contact(String sex, String firstName, String lastName, boolean isCompany, String companyName, String phone,
			String mobilePhone, String fax, String website, String email, List<Address> addresses) {
		super();
		this.sex = sex;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isCompany = isCompany;
		this.companyName = companyName;
		this.phone = phone;
		this.mobilePhone = mobilePhone;
		this.fax = fax;
		this.website = website;
		this.email = email;
		this.addresses = addresses;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Contact [sex=" + sex + ", firstName=" + firstName + ", lastName=" + lastName + ", isCompany="
				+ isCompany + ", companyName=" + companyName + ", phone=" + phone + ", mobilePhone=" + mobilePhone
				+ ", fax=" + fax + ", website=" + website + ", email=" + email + "Addresses=" + addresses + "]";
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Contact other = (Contact) object;

		if (this.id != other.id)
			return false;
	
		return true;
	}

	@Override
	public int compareTo(Contact o) {
		if(isCompany()) {
			return this.getCompanyName().compareTo(o.getCompanyName());
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

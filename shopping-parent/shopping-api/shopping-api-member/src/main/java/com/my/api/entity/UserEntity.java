package com.my.api.entity;



import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

		private Integer Id;
		private String username;
		private String password;
		private String phone;
		private String email;
		private Date created;
		private Date updated;
		private String openid;
	
}

package com.devsuperior.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.projections.UserDetailsProjection;
import com.devsuperior.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService{

	@Autowired
    private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		
		if(result.size() == 0) {			
			
			throw new UsernameNotFoundException("User not found");
		}
		
		//List<User> user = result.stream().map(x -> new User(x)).toList();
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		
		//user.addRole(result.stream(x -> new Role(x)));
		for(UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));	
		
			//result.stream().map(x -> new Role(x.getRoleId(), x.getAuthority())).forEach(user::addRole);
			
		}
		
		return user;
	}

}

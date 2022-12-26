package com.bezkoder.spring.security.jwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.security.jwt.exception.TokenRefreshException;
import com.bezkoder.spring.security.jwt.models.ERole;
import com.bezkoder.spring.security.jwt.models.RefreshToken;
import com.bezkoder.spring.security.jwt.models.Role;
import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.payload.request.UpdateRoleRequest;
//import com.bezkoder.spring.security.jwt.models.UserRole;
import com.bezkoder.spring.security.jwt.payload.response.MessageResponse;
import com.bezkoder.spring.security.jwt.payload.response.TokenRefreshResponse;
import com.bezkoder.spring.security.jwt.repository.RoleRepository;
import com.bezkoder.spring.security.jwt.repository.UserRepository;
//import com.bezkoder.spring.security.jwt.repository.UserRoleRepository;
import com.bezkoder.spring.security.jwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/role")
public class UserRoleController {

//	@Autowired
//	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

//	@PostMapping("/update")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<?> updateRole(@Valid @RequestBody UserRole userRole) {
//
//		return userRoleRepository.findById(userRole.getUserId()).map(user -> {
//			this.userRoleRepository.save(userRole);
//
//			return ResponseEntity.ok(new MessageResponse("Role updated successfully!"));
//		}).orElseThrow(() -> new RuntimeException("Error: Something went wrong while updating role."));
//	}

	@GetMapping("/rolelist")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Role>> getRoleList() {
		try {
			return new ResponseEntity<List<Role>>(this.roleRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException("Error: Role list not found.");
		}

	}

	@GetMapping("/userlist")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getUserList() {
		try {
			return new ResponseEntity<List<User>>(this.userRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException("Error: User list not found.");
		}

	}

	@PutMapping("/updateuser")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> updateUser(  @Valid @RequestBody UpdateRoleRequest user) {
		try {
           
			User users = this.userRepository.findByUsername(user.getUsername()).get();
			users.setEmail(user.getEmail());

			Set<Role> roles = new HashSet<>();
			Role userRole=null;
            if (user.getRole().equals("ROLE_ADMIN")) {
            	userRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));	
			}else if (user.getRole().equals("ROLE_MODERATOR"))
			{
				userRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			} else if (user.getRole().equals("ROLE_USER")) 
			{
				userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			}
			
			roles.add(userRole);

			

			users.setRoles(roles);
			return new ResponseEntity<User>(this.userRepository.save(users), HttpStatus.OK);

		} catch (Exception e) {
			throw new RuntimeException("Error: User  not found.");
		}

	}

	@GetMapping("/userdetails/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> getUserDetailsById(@PathVariable Long id) {
		try {
			return new ResponseEntity<User>(this.userRepository.findById(id).get(), HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException("Error: User  not found.");
		}

	}

}

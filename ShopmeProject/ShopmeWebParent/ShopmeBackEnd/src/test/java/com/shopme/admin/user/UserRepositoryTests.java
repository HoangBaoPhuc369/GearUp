package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	 
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userPhuc = new User("hoangbaophuc369@gmail.com", "$2y$04$sIbzhZBxwZ0AHkG2.q98Le5lJcA80JLlxNs8/vXwFSbuW3z7A4vIy", "Phuc", "Hoang");
		userPhuc.addRole(roleAdmin);
		
		User saveUser = repo.save(userPhuc);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userAnv = new User("nguyenvana@gmail.com", "123", "Nguyen Van", "A");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userAnv.addRole(roleEditor);
		userAnv.addRole(roleAssistant);
		
		User saveUser = repo.save(userAnv);
		
		assertThat(saveUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User userPhuc = repo.findById(1).get();
		System.out.println(userPhuc);	
		assertThat(userPhuc).isNotNull();
	}
	
	@Test 
	public void testUpdateUserDetails() {
		User userPhuc = repo.findById(1).get();
		userPhuc.setEnabled(true);
		userPhuc.setEmail("hoangbaophuc369@gmail.com");
		
		repo.save(userPhuc);
	}
	
	@Test 
	public void testUpdateUserRoles() {
		User userAnv = repo.findById(2).get();				
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userAnv.getRoles().remove(roleEditor);
		userAnv.addRole(roleSalesperson);
		
		repo.save(userAnv);
	}
	
	@Test 
	public void testDeleteUser() {
		Integer userId = 2;					
		repo.deleteById(userId);
		
	}
	
	@Test 
	public void testGetUserByEmail() {
		String email = "hoangbaophuc333@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
		
	}
	
	@Test 
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);	
		
		assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	
	@Test 
	public void testDisableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, false);			
		
	}
	
	@Test 
	public void testEnableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, true);			
		
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

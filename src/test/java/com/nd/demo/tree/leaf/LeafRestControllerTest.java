package com.nd.demo.tree.leaf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.demo.NdApplication;
import com.nd.demo.tree.leaf.CreateLeafDTO;
import com.nd.demo.tree.leaf.LeafEntity;
import com.nd.demo.tree.leaf.LeafRepository;
import com.nd.demo.tree.node.NodeEntity;
import com.nd.demo.tree.node.NodeRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { NdApplication.class })
@Transactional
@ActiveProfiles(value = "test")
public class LeafRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private LeafRepository leafRepository;
	@Autowired
	private NodeRepository nodeRepository;

	@Test
	public void create_withoutParent() throws Exception {
		CreateLeafDTO createLeafDTO = new CreateLeafDTO();
		createLeafDTO.setName("Leaf 1");

		ObjectMapper objectMapper = new ObjectMapper();
		String createLeafDTOJson = objectMapper.writeValueAsString(createLeafDTO);

		MvcResult result = mvc
				.perform(post("/api/leafs").contentType(MediaType.APPLICATION_JSON).content(createLeafDTOJson))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void create_withParent() throws Exception {
		NodeEntity parent = new NodeEntity();
		parent.setName("Leaf B");
		parent = nodeRepository.save(parent);

		CreateLeafDTO createLeafDTO = new CreateLeafDTO();
		createLeafDTO.setName("Leaf B.1");
		createLeafDTO.setParentId(parent.getId());

		ObjectMapper objectMapper = new ObjectMapper();
		String createLeafDTOJson = objectMapper.writeValueAsString(createLeafDTO);

		MvcResult result = mvc
				.perform(post("/api/leafs").contentType(MediaType.APPLICATION_JSON).content(createLeafDTOJson))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void getOne() throws Exception {
		LeafEntity leaf = new LeafEntity();
		leaf.setName("Leaf 1");
		leaf = leafRepository.save(leaf);
		mvc.perform(get("/api/leafs/" + leaf.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getAll() throws Exception {
		mvc.perform(get("/api/nodes").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void rename() throws Exception {
		LeafEntity leaf = new LeafEntity();
		leaf.setName("should be renamed");
		leaf = leafRepository.save(leaf);

		MvcResult result = mvc.perform(
				patch("/api/leafs/rename/" + leaf.getId()).contentType(MediaType.APPLICATION_JSON).content("new name"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		LeafEntity leafRenamed = leafRepository.findById(leaf.getId()).get();
		assertEquals("new name", leafRenamed.getName());
	}

	@Test
	public void changeParentId() throws Exception {
		NodeEntity parent = new NodeEntity();
		parent.setName("Node C");
		parent = nodeRepository.save(parent);

		LeafEntity child = new LeafEntity();
		child.setName("Leaf C.1");
		child.setParent(parent);
		child = leafRepository.save(child);

		NodeEntity newParent = new NodeEntity();
		newParent.setName("Node CC");
		newParent = nodeRepository.save(newParent);

		MvcResult result = mvc.perform(patch("/api/leafs/change-parent-id/" + child.getId())
				.contentType(MediaType.APPLICATION_JSON).content(String.valueOf(newParent.getId()))).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		LeafEntity childUpdated = leafRepository.findById(child.getId()).get();
		assertEquals(newParent.getId(), childUpdated.getParent().getId());
	}

	@Test
	public void delete() throws Exception {
		LeafEntity leaf = new LeafEntity();
		leaf.setName("should be deleted");
		leaf = leafRepository.save(leaf);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/api/leafs/" + leaf.getId())).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		Optional<LeafEntity> leafDeleted = leafRepository.findById(leaf.getId());
		assertFalse(leafDeleted.isPresent());
	}
}

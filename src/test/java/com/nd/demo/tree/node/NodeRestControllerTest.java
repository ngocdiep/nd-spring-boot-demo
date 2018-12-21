package com.nd.demo.tree.node;

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
import com.nd.demo.tree.leaf.LeafEntity;
import com.nd.demo.tree.leaf.LeafRepository;
import com.nd.demo.tree.node.CreateNodeDTO;
import com.nd.demo.tree.node.NodeEntity;
import com.nd.demo.tree.node.NodeRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { NdApplication.class })
@Transactional
@ActiveProfiles(value = "test")
public class NodeRestControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private NodeRepository nodeRepository;
	@Autowired
	private LeafRepository leafRepository;

	@Test
	public void create_withoutParent() throws Exception {
		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setName("Node A");

		ObjectMapper objectMapper = new ObjectMapper();
		String createNodeDTOJson = objectMapper.writeValueAsString(createNodeDTO);

		MvcResult result = mvc
				.perform(post("/api/nodes").contentType(MediaType.APPLICATION_JSON).content(createNodeDTOJson))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void create_withParent() throws Exception {
		NodeEntity parent = new NodeEntity();
		parent.setName("Node B");
		parent = nodeRepository.save(parent);

		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setName("Node B.1");
		createNodeDTO.setParentId(parent.getId());

		ObjectMapper objectMapper = new ObjectMapper();
		String createNodeDTOJson = objectMapper.writeValueAsString(createNodeDTO);

		MvcResult result = mvc
				.perform(post("/api/nodes").contentType(MediaType.APPLICATION_JSON).content(createNodeDTOJson))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}

	@Test
	public void getOne() throws Exception {
		NodeEntity node = new NodeEntity();
		node.setName("Node 1");
		node = nodeRepository.save(node);
		mvc.perform(get("/api/nodes/" + node.getId()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getAll() throws Exception {
		mvc.perform(get("/api/nodes").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void rename() throws Exception {
		NodeEntity node = new NodeEntity();
		node.setName("should be renamed");
		node = nodeRepository.save(node);

		MvcResult result = mvc.perform(
				patch("/api/nodes/rename/" + node.getId()).contentType(MediaType.APPLICATION_JSON).content("new name"))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		NodeEntity nodeRenamed = nodeRepository.findById(node.getId()).get();
		assertEquals("new name", nodeRenamed.getName());
	}

	@Test
	public void changeParentId() throws Exception {
		NodeEntity parent = new NodeEntity();
		parent.setName("Node C");
		parent = nodeRepository.save(parent);

		NodeEntity child = new NodeEntity();
		child.setName("Node C.1");
		child.setParent(parent);
		child = nodeRepository.save(child);

		NodeEntity newParent = new NodeEntity();
		newParent.setName("Node CC");
		newParent = nodeRepository.save(newParent);

		MvcResult result = mvc.perform(patch("/api/nodes/change-parent-id/" + child.getId())
				.contentType(MediaType.APPLICATION_JSON).content(String.valueOf(newParent.getId()))).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		NodeEntity childUpdated = nodeRepository.findById(child.getId()).get();
		assertEquals(newParent.getId(), childUpdated.getParent().getId());
	}

	@Test
	public void delete() throws Exception {
		NodeEntity node = new NodeEntity();
		node.setName("should be deleted");
		node = nodeRepository.save(node);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/api/nodes/" + node.getId())).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

		Optional<NodeEntity> nodeDeleted = nodeRepository.findById(node.getId());
		assertFalse(nodeDeleted.isPresent());
	}

	@Test
	public void getNodeTree() throws Exception {
		NodeEntity parent = new NodeEntity();
		parent.setName("Node D");
		parent = nodeRepository.save(parent);

		NodeEntity childLevel1 = new NodeEntity();
		childLevel1.setName("Node D.1");
		childLevel1.setParent(parent);
		childLevel1 = nodeRepository.save(childLevel1);

		LeafEntity leaf1 = new LeafEntity();
		leaf1.setName("Leaf D.1.1");
		leaf1.setParent(childLevel1);
		leafRepository.save(leaf1);

		childLevel1 = new NodeEntity();
		childLevel1.setName("Node D.2");
		childLevel1.setParent(parent);
		childLevel1 = nodeRepository.save(childLevel1);

		LeafEntity leaf2 = new LeafEntity();
		leaf2.setName("Leaf D.2.1");
		leaf2.setParent(childLevel1);
		leafRepository.save(leaf2);

		NodeEntity childLevel2 = new NodeEntity();
		childLevel2.setName("Node D.2.1");
		childLevel2.setParent(childLevel1);
		childLevel2 = nodeRepository.save(childLevel2);

		childLevel2 = new NodeEntity();
		childLevel2.setName("Node D.2.2");
		childLevel2.setParent(childLevel1);
		childLevel2 = nodeRepository.save(childLevel2);

		NodeEntity childLevel3 = new NodeEntity();
		childLevel3.setName("Node D.2.2.1");
		childLevel3.setParent(childLevel2);
		childLevel3 = nodeRepository.save(childLevel3);

		LeafEntity leaf3 = new LeafEntity();
		leaf3.setName("Leaf D.1.1");
		leaf3.setParent(childLevel3);
		leafRepository.save(leaf3);

		mvc.perform(get("/api/nodes/tree").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// TODO verify the test result
	}
}

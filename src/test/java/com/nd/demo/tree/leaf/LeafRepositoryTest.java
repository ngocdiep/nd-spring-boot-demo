package com.nd.demo.tree.leaf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.nd.demo.NdApplication;
import com.nd.demo.tree.leaf.LeafEntity;
import com.nd.demo.tree.leaf.LeafRepository;
import com.nd.demo.tree.node.NodeEntity;
import com.nd.demo.tree.node.NodeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { NdApplication.class })
@Transactional
@ActiveProfiles(value = "test")
public class LeafRepositoryTest {

	@Autowired
	private LeafRepository leafRepository;
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Test
	public void findByParentIdIn() {
		NodeEntity node1 = new NodeEntity();
		node1.setName("Node G");
		node1 = nodeRepository.save(node1);
		
		LeafEntity leaf1 = new LeafEntity();
		leaf1.setName("Leaf G");
		leaf1.setParent(node1);
		leafRepository.save(leaf1);
		
		NodeEntity node2 = new NodeEntity();
		node2.setName("Node H");
		node2 = nodeRepository.save(node2);
		
		LeafEntity leaf2 = new LeafEntity();
		leaf2.setName("Leaf H");
		leaf2.setParent(node2);
		leafRepository.save(leaf2);
		
		List<Long> nodeIds = new ArrayList<>();
		nodeIds.add(node1.getId());
		nodeIds.add(node2.getId());
		List<LeafEntity> leafs = leafRepository.findByParentIdIn(nodeIds);
		assertEquals(2, leafs.size());
	}
}

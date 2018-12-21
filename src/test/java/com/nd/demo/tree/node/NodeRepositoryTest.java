package com.nd.demo.tree.node;

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
import com.nd.demo.tree.node.NodeEntity;
import com.nd.demo.tree.node.NodeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { NdApplication.class })
@Transactional
@ActiveProfiles(value = "test")
public class NodeRepositoryTest {

	@Autowired
	private NodeRepository nodeRepository;

	@Test
	public void findByParentIdIn() {
		NodeEntity node0 = new NodeEntity();
		node0.setName("Node 0");
		node0 = nodeRepository.save(node0);

		NodeEntity node1 = new NodeEntity();
		node1.setName("Node G");
		node1 = nodeRepository.save(node1);

		NodeEntity node2 = new NodeEntity();
		node2.setName("Node H");
		node2.setParent(node1);
		node2 = nodeRepository.save(node2);

		NodeEntity node3 = new NodeEntity();
		node3.setName("Node I");
		node3.setParent(node2);
		node3 = nodeRepository.save(node3);

		List<Long> nodeIds = new ArrayList<>();
		nodeIds.add(node1.getId());
		nodeIds.add(node3.getId());
		List<NodeEntity> subNodes = nodeRepository.getSubNodesByParentId(node1.getId());
		assertEquals(2, subNodes.size());
	}
}

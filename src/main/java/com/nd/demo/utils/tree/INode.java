package com.nd.demo.utils.tree;

public interface INode<T> {
	Long getId();

	String getName();

	T getParent();

	String getParentPath();
}

#ifndef NODE_H
#define NODE_H

#include "Loc.h"
#include <iostream>

using namespace std;

class Node : public Loc {
public:
	Node* parent;
	bool open;
	bool closed;
	float cumulativeCost;
	float traversalCost;
	int manhattanToPathEnd;

	Node(int x, int y, float traversalCost, int manhattanToPathEnd) : Loc(x, y) {
		Node::traversalCost = traversalCost;
		Node::manhattanToPathEnd = manhattanToPathEnd;
		parent = nullptr;
		open = closed = false;
		cumulativeCost = 0;
	}

	bool operator<(Node& loc) const {
		float distAndCost = manhattanToPathEnd + cumulativeCost;
		float otherDistAndCost = loc.manhattanToPathEnd + loc.cumulativeCost;
		return distAndCost < otherDistAndCost;
	}
};

#endif
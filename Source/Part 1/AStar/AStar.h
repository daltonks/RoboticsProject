#ifndef ASTAR_H
#define ASTAR_H

#include <queue>
#include <vector>
#include <unordered_set>
#include "Grid.h"
#include "Node.h"
#include "PriorityVector.h"

class AStar {
private:
	Grid* grid;
	Loc start, end;
	Node *current, *closestToEnd;
	PriorityVector open;
	unordered_set<Node*, Loc, Loc> nodes;

public:
	AStar(Grid* grid, Loc start, Loc end) {
		AStar::grid = grid;
		AStar::start = start;
		AStar::end = end;
	}

	vector<Loc*>* findPath();

	~AStar() {
		delete current;
		delete closestToEnd;
	}

private:
	void testNeighbor(Node* currentNode, int x, int y);
	bool isTraversable(int x, int y);

	Node* getNode(int x, int y) {
		int manhattan = abs(x - end.getX()) + abs(y - end.getY());
		unordered_set<Node*>::const_iterator it = nodes.find(new Node(x, y, 0, 0));
		if(it == nodes.end()) {
			Node* node = new Node(x, y, grid->getTraversalCost(grid->get(x, y)), manhattan);
			nodes.insert(node);
			return node;
		} else {
			Node* node = *it;
			return *it;
		}
	}

	void markGoodNode(Node* node, float cumulativeCost);
};

#endif
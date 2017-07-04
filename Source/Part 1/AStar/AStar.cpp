#include <queue>
#include <vector>
#include "Grid.h"
#include "Node.h"
#include "AStar.h"
#include <cmath>
using namespace std;

vector<Loc*>* AStar::findPath() {
	Node* startNode = getNode(start.getX(), start.getY());
	startNode->open = true;
	open.add(startNode);
	closestToEnd = startNode;
	while(!open.empty()) {
		current = open.pop();
		
		if(*(current) == end) {
			break;
		}

		current->open = false;
		current->closed = true;

		//Test all neighbor tiles
		for (int x = current->getX() - 1; x <= current->getX() + 1; x++) {
			for (int y = current->getY() - 1; y <= current->getY() + 1; y++) {
				if (x != current->getX() || y != current->getY()) {
					testNeighbor(current, x, y);
				}
			}
		}
	}

	Node* endNode = getNode(end.getX(), end.getY());

	vector<Loc*>* path = new vector<Loc*>();
	while(closestToEnd != startNode) {
		path->insert(path->begin(), new Loc(closestToEnd->getX(), closestToEnd->getY()));
		closestToEnd = closestToEnd->parent;
	}
	path->insert(path->begin(), new Loc(closestToEnd->getX(), closestToEnd->getY()));
	return path;
}

void AStar::testNeighbor(Node* currentNode, int x, int y) {
	if (!isTraversable(x, y)) {
		return;
	}

	Node* neighborNode = getNode(x, y);
	if(neighborNode->closed) {
		return;
	}

	bool isDiagonal = x - currentNode->getX() != 0 && y - currentNode->getY() != 0;
	if (isDiagonal) {
		//Check if we will run into side collisions when traversing diagonals
		if (!isTraversable(x, currentNode->getY()) || !isTraversable(currentNode->getX(), y)) {
			return;
		}
	}
	float traversalCost = neighborNode->traversalCost;
	if (isDiagonal) {
		traversalCost *= 1.41421356237; //Approximation for square root of 2
	}
	float cumulativeCost = current->cumulativeCost + traversalCost;
	if(!neighborNode->open) {
		markGoodNode(neighborNode, cumulativeCost);
		neighborNode->open = true;
		open.add(neighborNode);
	} else if(cumulativeCost < neighborNode->cumulativeCost) {
		markGoodNode(neighborNode, cumulativeCost);
	}
}

bool AStar::isTraversable(int x, int y) {
	//Check if out of bounds
	if (x < 0 || y < 0 || x >= grid->getWidth() || y >= grid->getHeight()) {
		return false;
	}

	//Check if the neighbor has a traversalCost of -1
	Node* node = getNode(x, y);
	if (node->traversalCost == -1) {
		return false;
	}

	return true;
}

void AStar::markGoodNode(Node* node, float cumulativeCost) {
	node->cumulativeCost = cumulativeCost;
	node->parent = current;
	if(node->manhattanToPathEnd < closestToEnd->manhattanToPathEnd) {
		closestToEnd = node;
	}
}
#ifndef PRIORITY_VECTOR_H
#define PRIORITY_VECTOR_H

#include <vector>
#include "Node.h"

using namespace std;

class PriorityVector {
private:
	vector<Node*> locs;

public:
	void add(Node* node) {
		for(int i = 0; i < locs.size(); i++) {
			if(*node < *locs[i]) {
				locs.insert(locs.begin() + i, node);
				return;
			}
		}
		locs.push_back(node);
	}

	void remove(Node* node) {
		for(int i = 0; i < locs.size(); i++) {
			if(locs[i] == node) {
				locs.erase(locs.begin() + i);
				delete node;
				return;
			}
		}
	}

	Node* pop() {
		Node* node = locs[0];
		locs.erase(locs.begin());
		return node;
	}

	bool empty() {
		return locs.empty();
	}

	~PriorityVector() {
		for(int i = 0; i < locs.size(); i++) {
			delete locs[i];
		}
	}
};

#endif
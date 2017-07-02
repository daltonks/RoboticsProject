#ifndef LOC_H
#define LOC_H

using namespace std;

class Loc {
private:
	int x, y;
public:
	Loc() {
		x = y = 0;
	}

	Loc(int x, int y) {
		Loc::x = x;
		Loc::y = y;
	}

	int getX() const {
		return x;
	}

	int getY() const {
		return y;
	}

	bool operator==(Loc& loc) const {
		return getX() == loc.getX() && getY() == loc.getY();
	}

	bool operator()(Loc* loc1, Loc* loc2) const {
		return *loc1 == *loc2;
	}

	std::size_t operator()(const Loc* loc) const {
      size_t hash = 23;
	  hash = (hash * 37) + loc->getX();
	  hash = (hash * 37) + loc->getY();
	  return hash;
    }
};

#include "Node.h"

#endif
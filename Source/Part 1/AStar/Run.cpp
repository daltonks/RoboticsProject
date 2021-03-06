﻿#include "Grid.h"
#include "AStar.h"
#include <string>

using namespace std;

void main() {
	Grid* grid = new Grid(12, 12);
	grid->setTraversalCost('-', -1);
	grid->setTraversalCost('|', -1);
	grid->setTraversalCost('X', -1);
	grid->setTraversalCost(' ', 1);
	grid->setTraversalCost('S', 1);
	grid->setTraversalCost('E', 1);
	grid->set(
		"------------"
		"|          |"
		"|          |"
		"|       X  |"
		"|      X   |"
		"|  X XX    |"
		"|   X      |"
		"|          |"
		"|          |"
		"|          |"
		"|           "
		"------------");
	Loc start = Loc(1, 1);
	Loc end = Loc(11, 10);
	grid->set(start.getX(), start.getY(), 'S');
	grid->set(end.getX(), end.getY(), 'E');

	cout << "Starting grid:" << endl;
	grid->print();

	AStar* pathfinder = new AStar(grid, start, end);
	vector<Loc*>* path = pathfinder->findPath();
	for(int i = 0; i < path->size(); i++) {
		Loc* loc = (*path)[i];
		grid->set(loc->getX(), loc->getY(), '.');
	}
	
	cout << endl << "With pathfinding:" << endl;
	grid->print();

	cout << endl;

	system("pause");
}
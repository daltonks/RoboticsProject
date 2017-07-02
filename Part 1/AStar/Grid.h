#ifndef GRID_H
#define GRID_H

#include <vector>
#include "Loc.h"
#include <unordered_map>
#include <string>
#include <iostream>

using namespace std;

class Grid {
private:
	int width, height;
	char** grid;
	unordered_map<char, float>* charCosts;
public:
	Grid(int width, int height) {
		Grid::width = width;
		Grid::height = height;

		charCosts = new unordered_map<char, float>();

		grid = new char*[height];
		for (int i = 0; i < height; ++i) {
			grid[i] = new char[width];
		}
	}

	void print() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				cout << get(x, y);
			}
			cout << endl;
		}
	}

	void setTraversalCost(char c, float cost) {
		charCosts->insert(pair<char, float>(c, cost));
	}

	float getTraversalCost(char c) {
		return charCosts->find(c)->second;
	}

	void set(string str) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				set(x, y, str[y * width + x]);
			}
		}
	}

	void set(int x, int y, char c) {
		grid[y][x] = c;
	}

	char get(int x, int y) {
		return grid[y][x];
	}

	int getWidth() {
		return width;
	}

	int getHeight() {
		return height;
	}

	~Grid() {
		delete charCosts;

		for (int i = 0; i < height; ++i) {
			delete[] grid[i];
		}
		delete[] grid;
	}
};

#endif
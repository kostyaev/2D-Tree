2D-Tree
=======

2D-Tree is an implementation 2-dimensional tree for range and nearest neighbors searches.

### Tree configuration operations
 - `buildTree(List<Node<T>> nodes` - builds balanced 2d-tree 
 - `insert(T object, double x, double y)` - inserts object into the tree
 - `insertOrUpdate(T object, double x, double y)` - inserts object if it doesn't exists in the tree otherwise updates the existing object
 - `delete(T object)` - deletes object from the tree

### Tree search operations
 - `findNearestNeighbour(double x, double y)`  - finds the nearest neighbor 
 - `findNearestNeighbours(double x, double y, int k)` - finds k nearest neighbors
 - `rangeSearch(double x, double y, double radius)` - finds neighbors in specified range
 - `rangeSearchKm(double x, double y, double radius)` - finds neighbors in specified range assuming that `x` is a latitude, `y` - longitude, `radius` - radius in km.

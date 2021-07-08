package pouryapb;

/**
 * 
 * @author Pourya
 *
 * @param <E> - Element
 */
public class PriorityQueue<E> {

	private static class Node<E> {

		int priority;
		E value;

		Node<E> left = null;
		Node<E> right = null;
		Node<E> parrent;

		Node(E value, int priority, Node<E> parrent) {
			this.value = value;
			this.parrent = parrent;
			this.priority = priority;
		}
	}

	private Node<E> root = null;
	private int size = 0;

	/**
	 * adds a new node with given priority to the right place in queue.
	 * 
	 * @param value
	 * @param priority
	 */
	public void enqueue(E value, int priority) {

		Node<E> added = null;
		if (size == 0) {
			added = root = new Node<>(value, priority, null);
			size++;
		} else if (size == 1) {
			added = root.left = new Node<>(value, priority, root);
			size++;
		} else {
			size++;
			var path = Integer.toBinaryString(size);
			Node<E> node = root;

			for (var i = 1; i < path.length(); i++) {
				if (i != path.length() - 1) {
					if (path.charAt(i) == '0')
						node = node.left;
					else
						node = node.right;
				} else {
					if (path.charAt(i) == '0')
						added = node.left = new Node<>(value, priority, node);
					else
						added = node.right = new Node<>(value, priority, node);
				}
			}
		}

		while (added.parrent != null && added.priority > added.parrent.priority) {
			var tempV = added.parrent.value;
			int tempP = added.parrent.priority;
			added.parrent.value = added.value;
			added.parrent.priority = added.priority;
			added.value = tempV;
			added.priority = tempP;
			added = added.parrent;
		}
	}

	/**
	 * returns the element with highest priority and removes it from the queue.
	 * 
	 * @return The element with highest priority.
	 */
	public E dequeue() {

		Node<E> node;

		if (size > 1) {
			var path = Integer.toBinaryString(size);
			node = root;

			for (var i = 1; i < path.length(); i++) {
				if (path.charAt(i) == '0')
					node = node.left;
				else
					node = node.right;
			}
		} else if (size == 1)
			node = root;
		else
			return null;

		var result = root.value;

		root.value = node.value;
		root.priority = node.priority;
		deleteNode(node);

		Node<E> current = root;
		while (size > 1) {
			Node<E> bigger;
			if ((current.left != null && current.right != null && current.left.priority >= current.right.priority
					&& current.priority <= current.left.priority)
					|| (current.left != null && current.right == null && current.priority <= current.left.priority)) {
				bigger = current.left;
				swap(current, bigger);
				current = bigger;
			} else if ((current.left != null && current.right != null && current.left.priority <= current.right.priority
					&& current.priority <= current.right.priority)
					|| (current.left == null && current.right != null && current.priority <= current.right.priority)) {
				bigger = current.right;
				swap(current, bigger);
				current = bigger;
			} else {
				break;
			}
		}

		return result;
	}

	private void swap(Node<E> a, Node<E> b) {
		int tempP = a.priority;
		var tempV = a.value;
		a.priority = b.priority;
		a.value = b.value;
		b.priority = tempP;
		b.value = tempV;

	}

	private void deleteNode(Node<E> node) {

		Node<E> temp = node.parrent;
		if (temp == null) {
			root = null;
			size--;
			return;
		}
		if (temp.left != null && temp.left == node)
			temp.left = null;
		else if (temp.right != null && temp.right == node)
			temp.right = null;
		size--;
	}

	/**
	 * 
	 * @return size of the queue.
	 */
	public int size() {
		return size;
	}

	/**
	 * 
	 * @return true if list is empty else returns false.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	// test cases
	// public static void main(String[] args) {
	// PriorityQueue<Integer> a = new PriorityQueue<Integer>();
	// a.enqueue(4, 1);
	// a.enqueue(7, 5);
	// a.enqueue(2, 3);
	// a.enqueue(1, 2);
	// a.enqueue(85, 4);
	// System.out.println(a.dequeue());
	// System.out.println(a.dequeue());
	// System.out.println(a.dequeue());
	// System.out.println(a.dequeue());
	// System.out.println(a.dequeue());
	// }
}

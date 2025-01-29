/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {		
		Node newNode = freeList.getFirst();
		
		for (int i = 0; i < freeList.getSize(); i++) {
			if (newNode.block.length >= length) {
				MemoryBlock newAllocetedBlock = new MemoryBlock(newNode.block.baseAddress, length);
				allocatedList.addLast(newAllocetedBlock);
				newNode.block.length -= length;
				newNode.block.baseAddress += length;
				return newAllocetedBlock.baseAddress;
			} 
			else {
				newNode = newNode.next;
			}

		}
		//// Replace the following statement with your code
		return -1;
	}

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		ListIterator it = new ListIterator(allocatedList.getNode(0));
		while (it.hasNext()) {
			MemoryBlock current = it.next();
			if (current.baseAddress == address) {
				freeList.addLast(current);
				allocatedList.remove(current);
			}
		}
	}
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		int i = 0;
		while (i < this.freeList.getSize()) {
			MemoryBlock Block1 = freeList.getBlock(i);
			int sum = Block1.baseAddress + Block1.length;
			
			MemoryBlock Block2 = null;
			int j = 0;
			
			while (j < freeList.getSize()) {
				if (j == i) {
					j++;
				} else {
					if (freeList.getBlock(j).baseAddress == sum) {
						Block2 = freeList.getBlock(j);
						break;
					} else {
						j++;
					}
				}
			}
			
			if (Block2 != null) {
				freeList.getBlock(i).length += Block2.length;
				freeList.remove(Block2);
			} else {
				i++;
			}
		}
	}
}

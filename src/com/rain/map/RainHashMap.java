package com.rain.map;

import java.util.ArrayList;
import java.util.List;

import com.rain.map.RainHashMap.Entry;

public class RainHashMap<K,V> implements RainMap<K, V>{

	//默认数组长度
	private static int defaultLength = 16;
	
	//负载因子
	private static double defaultLoader = 0.75;
	
	private Entry<K,V>[] table = null;
	private int size = 0;
	
	 public RainHashMap(int length,double loader) {
		 defaultLength =length;
		 defaultLoader = loader;
		 table = new Entry[defaultLength];
	}
	 
	 public RainHashMap() {
		this(defaultLength,defaultLoader);
	}
	@Override
	public V put(K k, V v) {
		
		//在这里判断一下 size是否达到了扩容的标准
		if(size >= defaultLength*defaultLoader){
			up2size();
		}
		//首先创建一个hash函数,根据key的hash函数算出数组下标
		int index = getIndex(k);
		Entry<K,V> entry = table[index];
		
		if(entry == null){
			//如果entry为null  说明table的index位置上面没有元素
			table[index] = newEntry(k, v, null);
			size++;
		}else{//这个位置上面有值了的  那么久把新的存在这里 指针指向原来的entry
			table[index] = newEntry(k, v, entry);
		}
		return table[index].getValue();
	}
	
	//进行扩容
	private void up2size(){
		Entry<K,V> [] newTable = new Entry[2*defaultLength];
		//新创建数组以后 以前老数组里面的元素要对新数组进行再散列
		againHash(newTable);
	}
	
	//扩容进行的散列
	private void againHash(Entry<K, V>[] newTable) {
		
		List<Entry<K,V>> list = new ArrayList<>();
		for (int i = 0; i < table.length; i++) {
			if(table[i] == null){
				continue;
			}
			//newTable[i] = table[i]; 不行  如果是链表就不可取了
			foundEntryByNext(table[i],list);
		}
		
		if(list.size()>0){
			//要进行一个新数组的再散列
			size = 0;
			defaultLength = defaultLength*2;
			table = newTable;
			
			for (Entry<K, V> entry : list) {
				if(entry.next !=null){
					entry.next = null;
				}
				
				put(entry.getKey(), entry.getValue());
			}
		}
		
	}
	
	private void foundEntryByNext(Entry<K,V> entry,List<Entry<K,V>> list){
		if(entry!=null && entry.next != null){
			list.add(entry);
			foundEntryByNext(entry.next,list);
		}else{
			list.add(entry);
		}
	}

	private Entry<K,V> newEntry(K k,V v,Entry<K,V> next){
		return new Entry(k,v,next);
		
	}
	
	private int getIndex(K key){
		int m = defaultLength;
		int index = key.hashCode() % m;
		return index >= 0 ? index:-index;
	}

	@Override
	public V get(K k) {
		//首先根据key拿到对应的数组的位置
		int index = getIndex(k);
		
		if(table[index] == null){
			return null;
		}
		
		return findValueByEqualKey(k,table[index]);
	}
	
	public V findValueByEqualKey(K k, Entry<K,V> entry){
		if(k == entry.getKey() || k.equals(entry.getKey())){
			return entry.getValue();
		}else{
			if(entry.next != null){
				return findValueByEqualKey(k, entry.next);
			}
		}
		return null;
	}

	@Override
	public int size() {
		
		return size;
	}
	
	class Entry<K,V> implements RainMap.Entry<K, V>{
		
		public V value;
		public K key;
		public Entry<K,V> next;
		
		
		public Entry(K key,V value, Entry<K, V> next) {
			this.value = value;
			this.key = key;
			this.next = next;
		}

		@Override
		public K getKey() {
			
			return key;
		}

		@Override
		public V getValue() {
			
			return value;
		}
		
	}

}

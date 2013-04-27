package com.example.restful.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.example.restful.data.OrgClient.Collegue;
import com.example.restful.data.OrgClient.Department;



/**
 * @author simsun
 *
 */
public class memberCountCalculater {
	private static final String TAG = "memberCountCalculater";
	private final Boolean DEBUG = true;
	// tmp various for calculate member count of every department
	private final Map<Long, Set<Department>> Collegue2Department = Collections.synchronizedMap(new HashMap<Long, Set<Department>>());
	private final Map<Department, Set<Long>> fatherSet = Collections.synchronizedMap(new HashMap<Department, Set<Long>>());
	private final Map<Long, Integer> memberCount = Collections.synchronizedMap(new HashMap<Long, Integer>());
	private Department rootDepartment;
	
	public memberCountCalculater(Department root) {
		setRootDepartment(root);
	}
	
	public Department getRoot() {
		return rootDepartment;
	}
	
	public void dispose() {
		rootDepartment = null;
		Collegue2Department.clear();
		fatherSet.clear();
		memberCount.clear();
		System.gc();
	}
	/**
	 * interface for external call, get member count of target department
	 * @param department 
	 * @return member count
	 */
	public int calcMember(Department department)  {
		Integer count = memberCount.get(department.i);
		if (count != null) {
			return count;
		} else {
			return 0;
		}
	}
	
	/**
	 * save every department's father in a set. The set will wipe the same father node.
	 * @param department  
	 * @param father direct father of current department
	 */
	private void calcDepartmentFather(final Department department, final Department father) {
		
		for (Collegue member : department.m) {
			Set<Department> mDepSet = Collegue2Department.get(member.q);
			if (mDepSet == null) {
				mDepSet = new HashSet<OrgClient.Department>();
			} 
			mDepSet.add(department);
			
			Collegue2Department.put(member.q, mDepSet);
		}
		
		Set<Long> mSet = new HashSet<Long>();
		if (father != null) {
			mSet.addAll(fatherSet.get(father));
		}
		mSet.add(department.i);
		fatherSet.put(department, mSet);
		for (OrgClient.Department dp : department.o) {
			calcDepartmentFather(dp, department);
		}
	}
	
	/**
	 * calculate all collegue's department and then increase one to every department
	 */
	private void calcAllmemberCount() {
		
		for (Map.Entry<Long, Set<Department>> entry : Collegue2Department.entrySet()) {
			Set<Long> mfatherSet = new HashSet<Long>();
			for (Department department : entry.getValue()) {
				mfatherSet.addAll(fatherSet.get(department));
			}
			for (Long depUin : mfatherSet) {
				Integer count = memberCount.get(depUin);
						
				if (count == null)
					memberCount.put(depUin, 1);
				else 
					memberCount.put(depUin, count+1);
			}
		}
	}
	
	/**
	 * set current root department. 
	 * @param root
	 */
	private void setRootDepartment(Department root) {
		if (DEBUG) 	Log.d(TAG, "calcDepartmentFather start");
		calcDepartmentFather(root, null);
		if (DEBUG) 	Log.d(TAG, "calcDepartmentFather end");
		
		if (DEBUG)	Log.d(TAG, "calcAllmemberCount start");
		calcAllmemberCount();
		if (DEBUG)	Log.d(TAG, "calcAllmemberCount end");
	}
	
}

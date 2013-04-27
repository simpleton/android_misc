/**
 * 
 */
package com.example.restful.data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.restful.data.OrgClient.Collegue;
import com.example.restful.data.OrgClient.Department;
import com.example.restful.data.OrgContent.DB_Collegue;
import com.example.restful.data.OrgContent.DB_Department;
import com.example.restful.data.OrgContent.collegue_relation;
import com.example.restful.data.OrgContent.department_relation;

/**
 * @author simsun
 *
 */
public class OrgModelHelper {

	private memberCountCalculater memCounterCalc;
	private WeakReference<Department> mRootDepartment;
	private final WeakReference<Context> context;
	public OrgModelHelper(Context ct) {
		context = new WeakReference<Context>(ct);		
	}
	
	public void bulkInsertOrgInfo(Department root, String AccountUin) {
		Department weakroot = mRootDepartment.get();
		if (memCounterCalc == null || weakroot == null || weakroot != root) {
			memCounterCalc = new memberCountCalculater(root);
			mRootDepartment = new WeakReference<OrgClient.Department>(root);
		}
		
		Context ct = context.get();
		
		if (ct == null) 
			throw new NullPointerException("Context is NULL");
		List<ContentValues> departmentList = new ArrayList<ContentValues>();
		List<ContentValues> collegueList = new ArrayList<ContentValues>();
		List<ContentValues> collegueRelationList = new ArrayList<ContentValues>();
		List<ContentValues> departmentRelationList = new ArrayList<ContentValues>();
		buildContentValuesList(departmentList, collegueList, collegueRelationList, departmentRelationList, root);
		
		
		Uri dep_uri = Uri.parse("content://" + OrgProvider.AUTHORITY + "/" 
								+ AccountUin + "/" + DB_Department.TABLE_NAME);
		if (context.get() != null)
			OrgProvider.bulkInsert(dep_uri, 
					departmentList.toArray(new ContentValues[departmentList.size()]), 
					ct);
		
		Uri collegue_uri = Uri.parse("content://" + OrgProvider.AUTHORITY + "/" 
								+ AccountUin + "/" + DB_Collegue.TABLE_NAME);
		if (context.get() != null)
			OrgProvider.bulkInsert(collegue_uri, 
					collegueList.toArray(new ContentValues[collegueList.size()]), 
					ct);
		
		Uri collegue_relation_uri = Uri.parse("content://" + OrgProvider.AUTHORITY + "/"
					+ AccountUin + "/" + collegue_relation.TABLE_NAME);
		if (context.get() != null) 
			OrgProvider.bulkInsert(collegue_relation_uri,
					collegueRelationList.toArray(new ContentValues[collegueRelationList.size()]), 
					ct);
		
		Uri department_relation_uri = Uri.parse("content://" + OrgProvider.AUTHORITY + "/"
					+ AccountUin + "/" + department_relation.TABLE_NAME);
		if (context.get() != null) 
			OrgProvider.bulkInsert(department_relation_uri, 
					departmentRelationList.toArray(new ContentValues[departmentRelationList.size()]), 
					ct);
		
	}
	
	private void buildContentValuesList(List<ContentValues> departmentList, List<ContentValues> collegueList,
			List<ContentValues> collegueRelationList, List<ContentValues> departmentRelationList,
			Department root) {
		//insert root's member
		for (Collegue collegue : root.m) {
			final ContentValues contentValues = new ContentValues();
			contentValues.put(DB_Collegue.Columns.DUTY.getName(), collegue.j);
			contentValues.put(DB_Collegue.Columns.UIN.getName(), collegue.q);
			contentValues.put(DB_Collegue.Columns.GENDER.getName(), collegue.g);
			contentValues.put(DB_Collegue.Columns.MOBILE_PHONE.getName(), collegue.m);
			contentValues.put(DB_Collegue.Columns.NAME.getName(), collegue.n);
			contentValues.put(DB_Collegue.Columns.ENGLISH_NAME.getName(), collegue.u);
			contentValues.put(DB_Collegue.Columns.PHONE.getName(), collegue.p);
			contentValues.put(DB_Collegue.Columns.EMAIL.getName(), collegue.e);
			
			collegueList.add(contentValues);
			
			//insert collegue and department relationship
			final ContentValues conValues = new ContentValues();
			conValues.put(collegue_relation.Columns.COLLEGUE_UIN.getName(), collegue.q);
			conValues.put(collegue_relation.Columns.DEPARTMENT_UIN.getName(), root.i);
			collegueRelationList.add(conValues);
		}
		
		//insert root department node
		final ContentValues contentValues = new ContentValues();
		contentValues.put(DB_Department.Columns.UIN.getName(), root.i);
		contentValues.put(DB_Department.Columns.NAME.getName(), root.n);
		contentValues.put(DB_Department.Columns.COLLEGUE_COUNT.getName(), memCounterCalc.calcMember(root));
		
		
		departmentList.add(contentValues);
		
		//recursively visit root's child department
		for (Department department: root.o) {			
			//add by tyler, insert department relationship
			final ContentValues departmentContentValues = new ContentValues();
			departmentContentValues.put(department_relation.Columns.DEPARTMENT_UIN.getName(), root.i);
			departmentContentValues.put(department_relation.Columns.CHILD_DEPARTMENT_UIN.getName(), department.i); 
			departmentRelationList.add(departmentContentValues);
			
			buildContentValuesList(departmentList, collegueList, collegueRelationList, departmentRelationList, department);
		}
	}
}

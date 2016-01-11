package com.mec.fx;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.mec.fx.Person.AgeCategory;
import com.mec.resources.Msg;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

public class TreeTableUtil {

	@SuppressWarnings("unchecked")
	public static TreeItem<Person> getModel(){
		
		String itemDelimiter = Msg.get(TreeTableUtil.class, "person.delimiter");
		
		List<Person> persons = Msg.getList(TreeTableUtil.class, "person").stream().map(line ->{
			String[] fields = line.split(itemDelimiter);
			return new Person(
					fields[0].trim(),
					fields[1].trim(),
					LocalDate.of(Integer.parseInt(fields[2].trim()), Integer.parseInt(fields[3].trim()), Integer.parseInt(fields[4].trim()))
					);
		}).collect(Collectors.toList());
		
		int i = 0;
		TreeItem<Person> node1 = new TreeItem<>(persons.get(i++));
		node1.getChildren().addAll(new TreeItem<>(persons.get(i++)), new TreeItem<>(persons.get(i++)));
		TreeItem<Person> node2 = new TreeItem<>(persons.get(i++));
		node2.getChildren().addAll(new TreeItem<>(persons.get(i++)));
		
		TreeItem<Person> node3 = new TreeItem<>(persons.get(i++));
		node3.getChildren().addAll(node2, new TreeItem<>(persons.get(i++)));
		
		//
		TreeItem<Person> node4 = new TreeItem<>(persons.get(i++));
		node4.getChildren().addAll(new TreeItem<>(persons.get(i++)), new TreeItem<>(persons.get(i++)));
		
		//
		TreeItem<Person> node5 = new TreeItem<>(persons.get(i++));
		TreeItem<Person> node6 = new TreeItem<>(persons.get(i++));

		TreeItem<Person> rootNode = node1;
		rootNode.getChildren().addAll(node3, node4, node5, node6);
		return rootNode;
	}
	
	//Returns Person Id TreeTableColumn
	public static TreeTableColumn<Person, Integer> getIdColumn(){
		TreeTableColumn<Person, Integer> idCol = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.id.header"));;
		idCol.setCellValueFactory(new TreeItemPropertyValueFactory<>(Msg.get(TreeTableUtil.class, "column.id.property")));
		return idCol;
	}
	public static TreeTableColumn<Person, String> getfirstNameColumn(){
		TreeTableColumn<Person, String> col = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.firstName.header"));;
		col.setCellValueFactory(new TreeItemPropertyValueFactory<>(Msg.get(TreeTableUtil.class, "column.firstName.property")));
		return col;
	}
	public static TreeTableColumn<Person, String> getLastNameColumn(){
		TreeTableColumn<Person, String> col = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.lastName.header"));;
		col.setCellValueFactory(new TreeItemPropertyValueFactory<>(Msg.get(TreeTableUtil.class, "column.lastName.property")));
		return col;
	}
	public static TreeTableColumn<Person, LocalDate> getBirthDate(){
		TreeTableColumn<Person, LocalDate> col = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.birthDate.header"));;
		col.setCellValueFactory(new TreeItemPropertyValueFactory<>(Msg.get(TreeTableUtil.class, "column.birthDate.property")));
		return col;
	}
	public static TreeTableColumn<Person, Person.AgeCategory> getAgeCategoryColumn(){
		TreeTableColumn<Person, Person.AgeCategory> col = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.ageCategory.header"));;
		col.setCellValueFactory(new TreeItemPropertyValueFactory<>(Msg.get(TreeTableUtil.class, "column.ageCategory.property")));
		return col;
	}
	
	public static TreeTableColumn<Person, String> getAgeDescColumn(){
		TreeTableColumn<Person, String> col = new TreeTableColumn<>(Msg.get(TreeTableUtil.class, "column.ageDesc.header"));
		col.setCellValueFactory(cellData -> {
			Person p = cellData.getValue().getValue();
			LocalDate dob = p.getBirthDate();
			String ageInYear = Msg.get(TreeTableUtil.class, "ageDesc.unknown");
			if(null != dob){
				long years = ChronoUnit.YEARS.between(dob, LocalDate.now());
				if(0 == years){
					ageInYear = Msg.get(TreeTableUtil.class, "ageDesc.type1");
				}else if(1 == years){
					ageInYear = String.format(Msg.get(TreeTableUtil.class, "ageDesc.type2"), years);
				}else{
					ageInYear = String.format(Msg.get(TreeTableUtil.class, "ageDesc.type3"), years);
				}
					
			}
			return new ReadOnlyStringWrapper(ageInYear);
		});
		
		return col;
	}
}

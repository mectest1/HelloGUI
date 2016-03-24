package com.mec.application.beans;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.scene.control.ListCell;

@XmlRootElement
public class PatchReleaseConfigBean {
	String name;
	Path workspaceDirectory;
	String modifyList;
	Path patchReleaseDirectory;
	LocalDateTime patchTime = LocalDateTime.now();
	
	public PatchReleaseConfigBean() {
		super();
	}
	
	public PatchReleaseConfigBean(String name, Path workspaceDirectory, Path pathReleaseDirectory) {
		super();
		this.name = name;
		this.workspaceDirectory = workspaceDirectory;
		this.patchReleaseDirectory = pathReleaseDirectory;
	}
	public PatchReleaseConfigBean(String name, Path workspaceDirectory, String modifyList, Path pathReleaseDirectory) {
		super();
		this.name = name;
		this.workspaceDirectory = workspaceDirectory;
		this.modifyList = modifyList;
		this.patchReleaseDirectory = pathReleaseDirectory;
	}
	@Override
	public String toString() {
		return "PatchReleaseConfigBean [name=" + name + ", workspaceDirectory=" + workspaceDirectory + ", modifyList="
				+ modifyList + ", pathReleaseDirectory=" + patchReleaseDirectory + ", patchTime=" + patchTime + "]";
	}
	@XmlJavaTypeAdapter(PathXmlAdapter.class)
	public Path getWorkspaceDirectory() {
		return workspaceDirectory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setWorkspaceDirectory(Path workspaceDirectory) {
		this.workspaceDirectory = workspaceDirectory;
	}
	public String getModifyList() {
		return modifyList;
	}
	public void setModifyList(String modifyList) {
		this.modifyList = modifyList;
	}
	@XmlJavaTypeAdapter(PathXmlAdapter.class)
	public Path getPatchReleaseDirectory() {
		return patchReleaseDirectory;
	}
	public void setPatchReleaseDirectory(Path patchReleaseDirectory) {
		this.patchReleaseDirectory = patchReleaseDirectory;
	}
	@XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
	public LocalDateTime getPatchTime() {
		return patchTime;
	}
	public void setPatchTime(LocalDateTime patchTime) {
		this.patchTime = patchTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((patchReleaseDirectory == null) ? 0 : patchReleaseDirectory.hashCode());
		result = prime * result + ((workspaceDirectory == null) ? 0 : workspaceDirectory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatchReleaseConfigBean other = (PatchReleaseConfigBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (patchReleaseDirectory == null) {
			if (other.patchReleaseDirectory != null)
				return false;
		} else if (!patchReleaseDirectory.equals(other.patchReleaseDirectory))
			return false;
		if (workspaceDirectory == null) {
			if (other.workspaceDirectory != null)
				return false;
		} else if (!workspaceDirectory.equals(other.workspaceDirectory))
			return false;
		return true;
	}


	@XmlRootElement
	public static class PatchReleaseConfigBeans{
		List<PatchReleaseConfigBean> configs = new ArrayList<>();
		public PatchReleaseConfigBeans() {
			super();
		}
		public PatchReleaseConfigBeans(List<PatchReleaseConfigBean> configs) {
			super();
			this.configs = configs;
		}
		@XmlElement(name="PatchReleaseConfig")
		public List<PatchReleaseConfigBean> getConfigs() {
			return configs;
		}
		public void setConfigs(List<PatchReleaseConfigBean> configs) {
			this.configs = configs;
		}
	}
	
	public static class PatchReleaseConfigBeanListCell extends ListCell<PatchReleaseConfigBean>{
		@Override
		protected void updateItem(PatchReleaseConfigBean item, boolean empty) {
			super.updateItem(item, empty);
			if(empty){
				setText("");
			}else{
				setText(item.getName());
			}
		}
	}
}

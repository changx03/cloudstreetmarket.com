package edu.zipcloud.cloudstreetmarket.core.entities;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.zipcloud.cloudstreetmarket.core.converters.IdentifiableSerializer;
import edu.zipcloud.cloudstreetmarket.core.converters.IdentifiableToIdConverter;
import edu.zipcloud.cloudstreetmarket.core.enums.UserActivityType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "action_type")
@Table(name="user_action")
public class Action extends AbstractAutogeneratedId<Long>{

	Action(){
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_name")
	@JsonSerialize(using=IdentifiableSerializer.class)
	@JsonProperty("userId")
	@XStreamConverter(value=IdentifiableToIdConverter.class, strings={"id"})
	@XStreamAlias("userId")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Enumerated(EnumType.STRING)
	private UserActivityType type;

	@OneToMany(mappedBy = "targetAction", cascade = { CascadeType.ALL }, orphanRemoval=true, fetch=FetchType.LAZY)
	@JsonIgnore
	@XStreamOmitField
	private Set<LikeAction> likeActions = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "targetAction", cascade = { CascadeType.ALL }, orphanRemoval=true, fetch=FetchType.LAZY)
	@JsonIgnore
	@XStreamOmitField
	private Set<CommentAction> commentActions = new LinkedHashSet<>();
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserActivityType getType() {
		return type;
	}

	public void setType(UserActivityType type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Set<LikeAction> getLikeActions() {
		return likeActions;
	}
	
	public void setLikeActions(Set<LikeAction> likeActions) {
		this.likeActions = likeActions;
	}
	
	public Set<CommentAction> getCommentActions() {
		return commentActions;
	}
	
	public void setCommentActions(Set<CommentAction> commentActions) {
		this.commentActions = commentActions;
	}

	Action(Long id){
		this.id = id;
	}
	
	//Avoid fetching lazy collections at this stage (session may be closed)
	@Override
	public String toString() {
		return "Action [date=" + date + ", type=" + type + ", id=" + id + "]";
	}
}

package com.bitwise.app.graph.model;

import java.util.Comparator;

/**
 * This class is used to sort the links on basis of link's source Y location
 * 
 * @author Bitwise
 */
public class LinkComparatorBySourceLocation implements Comparator<Link> {

	@Override
	public int compare(Link firstLink, Link secondLink) {
		int firstLinkSourceYLocation = firstLink.getSource().getLocation().y;
		int secondLinkSourceYLocation = secondLink.getSource().getLocation().y;

		if (firstLinkSourceYLocation == secondLinkSourceYLocation) {
			if (firstLink.getSource().equals(secondLink.getSource())) {
				return compareTargetLinks(firstLink, secondLink);
			} else
				return 0;
		} else if (firstLinkSourceYLocation > secondLinkSourceYLocation)
			return 1;
		else
			return -1;
	}

	private int compareTargetLinks(Link firstLink, Link secondLink) {
		int firstLinkTargetYLocation = firstLink.getTarget().getLocation().y;
		int secondLinkTargetYLocation = secondLink.getTarget().getLocation().y;
		
		if (firstLinkTargetYLocation == secondLinkTargetYLocation) {
			return 0;
		} else if (firstLinkTargetYLocation > secondLinkTargetYLocation)
			return 1;
		else
			return -1;
	}
}

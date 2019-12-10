import java.util.*;

public class AdditiveExpression implements CompoundExpression {
	
	private CompoundExpression parent;
	private List<Expression> children;
	
	public AdditiveExpression() {
		children = new ArrayList<Expression>();
	}
	
	public CompoundExpression getParent() {		
		return parent;
	}

	public void setParent(CompoundExpression parent) {
		this.parent = parent;
	}

	public Expression deepCopy() {
		CompoundExpression newExp = new AdditiveExpression();
		for(Expression e : children) {
			Expression childCopy = e.deepCopy();
			childCopy.setParent(newExp);
			newExp.addSubexpression(childCopy);
		}
		return newExp;
	}

	public void flatten() {
		/*List<Expression> newChildren = new ArrayList<Expression>();
		for (int i = 0; i < children.size(); i ++) {
			children.get(i).flatten();
			if (children.get(i) instanceof AdditiveExpression) {
				for (Expression grandChild : ((AdditiveExpression) children.get(i)).children) {
					grandChild.setParent(this);
					newChildren.add(grandChild);
				}
				try {
					children.get(i).setParent(null);
				}
				catch (NullPointerException e){
					//Nothing
				}
			}
			else {
				newChildren.add(children.get(i));
			}
		} */
		Iterator<Expression> it = children.iterator();
		List<Expression> newChildren = new ArrayList<Expression>();
		int index = 1;
		int oldSize = children.size();
		
		while(it.hasNext() && index <= oldSize) {
			Expression e = it.next();
			index++;
			if(e instanceof AdditiveExpression) {
				for(Expression eofC : ((AdditiveExpression) e).children) {
					if(eofC instanceof AdditiveExpression) {
						for(Expression eofCC : ((AdditiveExpression) eofC).children) {
							eofCC.flatten();
							newChildren.add(eofCC);							
						}						
					}
					else {
						eofC.flatten();
						newChildren.add(eofC);					
					}
				}
				it.remove();
			}
			else {
				newChildren.add(e);
			}			
		}
		children = newChildren;
	}
	
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        for (int i = 0; i < indentLevel; i ++) {
            stringBuilder.append('\t');
        }
        stringBuilder.append('+');
        stringBuilder.append('\n');
        for (int i = 0; i < children.size(); i ++) {
            children.get(i).convertToString(stringBuilder, indentLevel + 1);
        }
    }
	
	public void addSubexpression(Expression subexpression) {
		subexpression.setParent(this);
		children.add(subexpression);
	}
}

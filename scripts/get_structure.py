import os
import sqlite3

dir = "/home/kcunning/projects/android-django/data/trunk/django"
db = "data.db"
ignore = ['locale', 'localflavor']

def get_sub_dir(parent, dirs):
	pass

def write_sub_nodes(c, parent_id, dirs):
	for d in dirs:
		c.execute("insert into node (_id, name, parent_id) values (null, '%s', %s)" % (d, parent_id))
	return c

def clean_dirs(dirs):
	for d in dirs[1]:
		if d.startswith("."):
			dirs[1].remove(d)
	for i in ignore:
		try:
			dirs[1].remove(i)
		except:
			pass
	return dirs

def get_db():
	try:
		os.remove(db)
	except:
		print "No database file. Continuing on."
	c = sqlite3.connect(db)
	c.execute('CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT \'en_US\')')
	c.execute('CREATE TABLE node (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, parent_id INTEGER)')	
	return c



def get_dirs(w, c):
	count = 0
	while True:
		try:
			parent_id = c.execute('select * from node where name = "%s"' % grandparent)
			r = c.execute('select last_insert_rowid()')
			parent_id = str(r.fetchone()[0])
			parent_id = parent_id.pop()[0]
		except:
			parent_id = "null"
			grandparent = "null"
		try:
			i = w.next()
		except:
			break
		i = clean_dirs(i)
		grandparent = i[0].split('/').pop()
		if i[1]:
			parent = i[0].split('/').pop()
			print "****************%s*****************" % grandparent
			for item in i[1]:
				
				sql = "insert into node (_id, name, parent_id) values (null, '%s', %s)" % (item, parent_id)
				print count, sql
				c.execute(sql)
	


def main():
	c = get_db()
	w = os.walk(dir)
	get_dirs(w, c)
	#p, dirs, files = i.next()
	#c.execute('insert into node (_id, name, parent_id) values (null, "django", null)')
	#dirs = clean_dirs(dirs)
	#for d in dirs:
	#	c.execute("insert into node (_id, name, parent_id) values (null, '%s', %s)" % (d, parent_id))
	#	
	c.commit()
	c.close()

if __name__ == "__main__":
	main()


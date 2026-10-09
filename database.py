from sys import argv
#this function creates a table and saves it in the dictionary
def create_table(table_name: str, columns: str, tables: dict):
    tables[table_name] = [columns.split(",")]
    print("###################### CREATE #########################")
    print(f"Table '{table_name}' created with columns: {columns.split(',')}")
    print("#######################################################")
    print()
    return tables
#this function used to print the table
def output(table_name,tables):
    table = tables[table_name]
    columns = table[0]
    #This function compares the lengths to create the table correctly
    max_len = [max(len(str(item[i])) for item in table) for i in range(len(columns))]
    #print borders

    title = "+" + "+".join("-" * (width + 2)  for width in max_len)
    #print tables data
    print_data = "|" + "|".join(f" {{:<{width}}} " for width in max_len) + "|"
    print(f"Table: {table_name}")
    print(title,end="")
    print("+")
    print(print_data.format(*columns))
    print(title,end="")
    print("+")
    for item in table[1:]:
        print(print_data.format(*item))
    print(title,end="")
    print("+")
    print("#######################################################")
    print()
    return tables
#This function saves the entered data
def insert(table_name: str, data: str, tables: dict):

    try:
        tables[table_name]
    #This command gives an error if a non-existent table name is entered.
    except KeyError:
        # print error
        print("###################### INSERT #########################")
        print(f"Table '{table_name}' not found")
        print(f"Inserted into ’{table_name}’: {tuple(data.split(','))}")
        print("#######################################################")
        print()
        return tables
    else:

        tables[table_name].append(data.split(','))
        #print data
        print("###################### INSERT #########################")
        print(f"Inserted into ’{table_name}’: {tuple(data.split(','))}\n")
        output(table_name,tables)
        return tables
def select(table_name, columns, conditions, tables):
    try:
        tables[table_name]
    # This command gives an error if a non-existent table name is entered.
    except KeyError:
        # print error
        print("###################### SELECT #########################")
        conditions = " ".join(conditions)
        print(f"Table {table_name} not found")
        print("Condition: ", conditions, end="")
        print(f"Select result from '{table_name}': None")
        print("#######################################################")
        print()
        return tables
    else:
        try:
            #This command finds errors in the entered expressions.
            table_columns = tables[table_name][0]
            wanted_indexes_from_columns = []
            if "*" in columns:
                for ind in range(0, len(table_columns)):
                    wanted_indexes_from_columns.append(ind)
            else:
                for column in columns:
                    wanted_indexes_from_columns.append(table_columns.index(column))
        except ValueError:
            # print error
            conditions = " ".join(conditions)
            print("###################### SELECT #########################")
            print(f"Column {column} does not exist")
            print("Condition:", conditions, end="")
            print(f"Select result from '{table_name}': None")
            print("#######################################################")
            print()
            return tables
        else:
            # This command makes the entered expressions available
            table_columns = tables[table_name][0]
            wanted_indexes_from_columns = []
            if "*" in columns :
                for ind in range(0, len(table_columns)):
                    wanted_indexes_from_columns.append(ind)
            else:
                for column in columns:
                    wanted_indexes_from_columns.append(table_columns.index(column))
            conditions = " ".join(conditions)
            wanted_conditions = conditions[1:-1].split(",")
            wanted_conditions_check = []
            try:
                # This command finds errors in the entered expressions.
                for condition in wanted_conditions:
                    key, value = condition.split(":")
                    key = key.strip('" {}'"'")
                    condition_index = list(table_columns).index(key)
            except ValueError:
                # print error
                print("###################### SELECT #########################")
                print(f"Column {key} does not exist")
                print("Condition:", conditions, end="")
                print(f"Select result from '{table_name}': None")
                print("#######################################################")
                print()
                return tables
            else:

                # This command makes the entered expressions available
                for condition in wanted_conditions:
                    key, value = condition.split(":")
                    key = key.strip('" {}'"'")
                    value = value.strip('" {}'"'")
                    condition_index = list(table_columns).index(key)
                    wanted_conditions_check.append([value, condition_index])
                comp = tables[table_name][1:].copy()

                for val in wanted_conditions_check:
                    val=str(val[0])

                    for item in comp[:]:
                        if val not in item:
                            comp.remove(item)
                result=[]
                for val in comp:
                    support_list = []
                    for num in wanted_indexes_from_columns:
                        support_list.append(val[num])
                    result.append(tuple(support_list))
                    # print data
                print("###################### SELECT #########################")
                print("Condition:",conditions,end="")
                print(f"Select result from '{table_name}':",result)
                print("#######################################################")
                print()
                return tables
#This function updates the requested things
def update(table_name, updates, conditions, tables):
    try:
        # This command gives an error if a non-existent table name is entered.
        table = tables[table_name]
    except KeyError:
        # print error
        conditions = " ".join(conditions)
        updates = " ".join(updates)
        print("###################### UPDATE #########################")
        print(f"Updated ’{table_name}’ with {updates} where {conditions}", end="")
        print(f"Table {table_name} not found")
        print("0 rows updated.")
        print("#######################################################")
        print()
        return tables
    table_columns = tables[table_name][0]
    conditions = " ".join(conditions)
    updates = " ".join(updates)
    wanted_updates =updates[1:-1].split(',')


    wanted_updates_check=[]
    try:
        # This command finds errors in the entered expressions.
        for update in wanted_updates:
            key, value = update.split(":")
            key = key.strip('" {]'"'")
            update_index = list(table_columns).index(key)
    except ValueError:
        # print error
        print("###################### UPDATE #########################")
        print(f"Updated ’{table_name}’ with {updates} where {conditions}", end="")
        print(f"Column {key} does not exist")
        print("0 rows updated.\n")
        output(table_name,tables)
        return tables

    else:
        # This command makes the entered expressions available
        for update in wanted_updates:
            key, value = update.split(":")
            key = key.strip('" {]'"'")
            value = value.strip('" {]'"'")
            update_index = list(table_columns).index(key)
            wanted_updates_check.append([value, update_index])

        wanted_conditions = conditions[1:-1].split(",")

        wanted_conditions_check = []

        try:
            # This command finds errors in the entered expressions.
            for condition in wanted_conditions:
                key, value = condition.split(":")
                key = key.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
        except ValueError:
            # print error
            print("###################### UPDATE #########################")
            print(f"Updated ’{table_name}’ with {updates} where {conditions}", end="")
            print(f"Column {key} does not exist")
            print("0 rows updated.\n")
            output(table_name, tables)
            return tables
        else:
            # This command makes the entered expressions available
            for condition in wanted_conditions:
                key, value = condition.split(":")
                key = key.strip('" {}'"'")
                value = value.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
                wanted_conditions_check.append([value, condition_index])
            comp = tables[table_name][1:].copy()
            for val in wanted_conditions_check:
                # This command makes the entered expressions available
                val=str(val[0])
                for item in comp[:]:
                    if val not in item:
                        comp.remove(item)
            chance_indextables=[]
            for ind in comp:
                chance_indextables.append(tables[table_name].index(ind))
            for it in wanted_updates_check:
                for ind in chance_indextables:
                    tables[table_name][ind][it[1]]=it[0]
            # Print data
            print("###################### UPDATE #########################")
            print(f"Updated ’{table_name}’ with {updates} where {conditions}",end="")
            print(len(comp),"rows updated.")
            output(table_name,tables)
            return tables
#This function deletes desired items
def delete(table_name, conditions, tables):
    try:
        # This command gives an error if a non-existent table name is entered.
        table_columns = tables[table_name][0]
    except KeyError:
        # print error
        conditions = " ".join(conditions)
        print("###################### DELETE #########################")
        print(f"Deleted from '{table_name}' where {conditions}", end="")
        print(f"Table {table_name} not found")
        print("0 rows deleted.")
        print("#######################################################")
        print()
        return tables
    else:
        # This command makes the entered expressions available
        table_columns = tables[table_name][0]

        if len(conditions) > 0:

            conditions = " ".join(conditions)
            wanted_conditions = conditions[1:-1].split(",")

            wanted_conditions_check = []

            try:
                # This command finds errors in the entered expressions.
                for condition in wanted_conditions:

                    key, value = condition.split(":")
                    key = key.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
            except ValueError:
                # print error
                print("###################### DELETE #########################")
                print(f"Deleted from '{table_name}' where {conditions}",end="")
                print(f"Column {key} does not exist")
                print("0 rows deleted.\n")
                output(table_name, tables)
                return tables
                # This command makes the entered expressions available
            for condition in wanted_conditions:
                key, value = condition.split(":")
                key = key.strip('" {}'"'")
                value = value.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
                wanted_conditions_check.append([value, condition_index])
            comp = tables[table_name][1:].copy()
            for val in wanted_conditions_check:
                val=str(val[0])
                for item in comp[:]:
                    if val in item:
                        comp.remove(item)
            comp.insert(0,tables[table_name][0])
            table = comp
            columns = table[0]

            max_len = [max(len(str(item[i])) for item in table) for i in range(len(columns))]

            title = "+" + "+".join("-" * (element + 2)  for element in max_len)
            #print data
            # I cannot use output() function that I defined because this is a bit different from the others
            print_data = "|" + "|".join(f" {{:<{element}}} " for element in max_len) + "|"
            print("###################### DELETE #########################")
            print(f"Deleted from '{table_name}' where {conditions}",end="")
            print(len(tables[table_name])-len(comp),"rows deleted.")
            print(f"Table: {table_name}")
            print(title,end="")
            print("+")
            print(print_data.format(*columns))
            print(title,end="")
            print("+")
            for item in table[1:]:
                print(print_data.format(*item))
            print(title,end="")
            print("+")
            print("#######################################################")
            print()
            return tables
        else:
            # If you want to delete everything, this command works
            columns = table_columns
            title = "+" + "+".join("-" * (len(element) + 2)  for element in table_columns)
            max_len = "|" + "|".join(f" {{:<{len(element)}}} " for element in table_columns) + "|"
            print("###################### DELETE #########################")
            print(f"Deleted from '{table_name}' where everwhere",end="")
            print(len(tables[table_name])-1,"rows deleted.")
            print(f"Table: {table_name}")
            print(title,end="")
            print("+")
            print(max_len.format(*columns))
            print(title,end="")
            print("+")
            print("#######################################################")
            print()
            return tables
#this command is used to merge two tables
def join(table1, table2, column, tables):
    try:
        # This command gives an error if a non-existent table name is entered.
        big_column = []
        for col in tables[table1][0]:
            big_column.append(col)
    except KeyError:
        # print error
        print("####################### JOIN ##########################")
        print(f"Joın tables {table1} and {table2}")
        print(f"Table {table1} does not exist")
        print("#######################################################\n")
        return tables
    else:
        try:
            # This command gives an error if a non-existent table name is entered.
            big_column = []
            for col in tables[table2][0]:
                big_column.append(col)
        except KeyError:
            # print error
            print("####################### JOIN ##########################")
            print(f"Joın tables {table1} and {table2}")
            print(f"Table {table2} does not exist")
            print("#######################################################\n")
            return tables
        else:
            try:
                #Finds if there is an error in the column to be merged
                big_column = []
                for col in tables[table1][0]:
                    big_column.append(col)
                for col in tables[table2][0]:
                    big_column.append(col)

                join_tables = []
                same_column1 = tables[table1][0].index(column)
                same_column2 = tables[table2][0].index(column)
            except ValueError:
                # print error
                print("####################### JOIN ##########################")
                print(f"Joın tables {table1} and {table2}")
                print(f"Column {column} does not exist")
                print("#######################################################\n")
                return tables
            else:
                # this command  combines the elements common to two tables in the column to be merged.
                big_column=[]
                for col in tables[table1][0]:
                    big_column.append(col)
                for col in tables[table2][0]:
                    big_column.append(col)

                join_tables=[]
                #Creates a table for things to be combined
                same_column1=tables[table1][0].index(column)
                same_column2=tables[table2][0].index(column)

                for item in tables[table1][1:]:
                    for item2 in  tables[table2][1:]:
                        if item[same_column1]==item2[same_column2]:
                            elements = []
                            for element in item:
                                elements.append(element)
                            for element2 in item2:
                                elements.append(element2)
                            join_tables.append(elements)
                join_tables.insert(0,big_column)
                table = join_tables
                column = join_tables[0]
                # I cannot use output() function that I defined because this is a bit different from the others
                max_len = [max(len(str(item[i])) for item in table) for i in range(len(column))]
                title = "+" + "+".join("-" * (element + 2)  for element in max_len)
                print_data = "|" + "|".join(f" {{:<{element}}} " for element in max_len) + "|"
                #print data
                print("####################### JOIN ##########################")
                print(f"Joın tables {table1} and {table2}")
                print(f"Join result ({len(join_tables)-1} rows):\n")
                print(f"Table: Joined table")
                print(title,end="")
                print("+")
                print(print_data.format(*column))
                print(title,end="")
                print("+")
                for item in table[1:]:
                    print(print_data.format(*item))
                print(title,end="")
                print("+")
                print("#######################################################")
                print()
                return tables
#This command shows how many of the desired expressions are on the desired table.
def count(table_name, conditions, tables):
    try:
        # This command gives an error if a non-existent table name is entered.
        table_columns = tables[table_name][0]
    except KeyError:
        # print error
        print("###################### COUNT #########################")
        print(f"Table {table_name} not found")
        print(f"Total number of entries in '{table_name}' is 0")
        print("#######################################################\n")
        return tables
    else:
        # This command makes the entered expressions available
        table_columns = tables[table_name][0]
        conditions = " ".join(conditions)
        wanted_conditions = conditions[1:-1].split(",")

        wanted_conditions_check = []
        all_conditions_passed = True
        try:
            # This command finds errors in the entered expressions.
            for condition in wanted_conditions:
                key, value = condition.split(":")
                key = key.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
        except ValueError:
            #print error
            print("###################### COUNT #########################")
            print(f"Column {key} does not exist")
            print(f"Total number of entries in '{table_name}' is 0")
            print("#######################################################")
            print()
            return tables
        else:
            # This command makes the entered expressions available
            for condition in wanted_conditions:
                key, value = condition.split(":")
                key = key.strip('" {}'"'")
                value = value.strip('" {}'"'")
                condition_index = list(table_columns).index(key)
                wanted_conditions_check.append([value, condition_index])
            comp = tables[table_name][1:].copy()

            for val in wanted_conditions_check:
                val=str(val[0])
                for item in comp[:]:
                    if val not in item:
                        comp.remove(item)
            #print data
            print("###################### COUNT #########################")
            print("Count:",len(comp))
            print(f"Total number of entries in '{table_name}' is {len(comp)}")
            print("#######################################################")
            print()
            return tables
#All functions are called from main
def main():
    f = open(argv[1], 'r')
    lines = f.readlines()

    tables = {}
    for line in lines:
        if "CREATE_TABLE" in line:
            arrays = line.split(" ")
            op, table_name, columns = arrays[0], arrays[1], " ".join(arrays[2:])
            if "\n" in columns:
                columns = columns[:-1]
            tables = create_table(table_name, columns, tables)
        elif "INSERT" in line:
            arrays = line.split(" ")
            op, table_name, columns = arrays[0], arrays[1], " ".join(arrays[2:])
            if "\n" in columns:
                columns = columns[:-1]
            tables = insert(table_name, columns, tables)
        elif "SELECT" in line:
            arrays = line.split(" ")
            op, table_name, columns, conditions = arrays[0], arrays[1] ,arrays[2].split(",") , arrays[4:]
            if "\n" in conditions:
                conditions = conditions[:-1]
            tables = select(table_name,columns, conditions, tables)
        elif "UPDATE" in line:
            arrays = line.split(" ")
            i_where=arrays.index("WHERE")
            op, table_name, uptates, conditions = arrays[0], arrays[1],arrays[2:i_where],arrays[i_where+1:]
            tables = update(table_name,uptates,conditions,tables)
        elif "DELETE" in line:
            arrays = line.split(" ")
            if len(arrays)==3:
                op, table_name,conditions = arrays[0], arrays[1],[]
                tables=delete(table_name, conditions,tables)
            else:
                op, table_name, conditions = arrays[0], arrays[1] ,arrays[3:]
                if "\n" in conditions:
                    conditions = conditions[:-1]
            tables = delete(table_name, conditions,tables)
        elif "JOIN" in line:
            arrays = line.split(" ")
            table=arrays[1].split(",")
            op,table1,table2,column,= arrays[0],table[0],table[1], arrays[3]
            if "\n" in column:
                column = column[:-1]
            tables = join(table1,table2,column,tables)
        elif "COUNT" in line:
            arrays = line.split(" ")
            op, table_name,conditions= arrays[0], arrays[1], arrays[3:]
            if "\n" in conditions:
                conditions = conditions[:-1]
            tables = count(table_name, conditions,tables)
    f.close()


if __name__ == "__main__":
    main()